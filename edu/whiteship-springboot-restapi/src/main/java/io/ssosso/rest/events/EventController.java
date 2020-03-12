package io.ssosso.rest.events;

import io.ssosso.rest.accounts.Account;
import io.ssosso.rest.accounts.AccountAdapter;
import io.ssosso.rest.accounts.CurrentUser;
import io.ssosso.rest.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

  private final EventRepository eventRepository;

  private final ModelMapper modelMapper;

  private final EventValidator eventValidator;

  public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
    this.eventRepository = eventRepository;
    this.modelMapper = modelMapper;
    this.eventValidator = eventValidator;
  }

  @PostMapping
  public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto,
                                    Errors errors,
                                    @CurrentUser Account currentUser) {

    // 옮기는 작업이 필요하나.. ModelMapper 를 통해 해결 할 수 있음.
//    Event event1
//        = Event.builder()
//        .name(event.getName())
//        .build();

    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(errors);
    }

    eventValidator.validate(eventDto, errors);

    Event event = modelMapper.map(eventDto, Event.class);
    event.update();
    event.setManager(currentUser);
    Event newEvent = this.eventRepository.save(event);

    final ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
    URI uri = selfLinkBuilder.toUri();

    EventResource eventResource = new EventResource(event);
    eventResource.add(linkTo(EventController.class).withRel("query-events"));
    eventResource.add(selfLinkBuilder.withSelfRel());
    eventResource.add(selfLinkBuilder.withRel("update-event"));
    eventResource.add(new Link("/docs/index.html#resources-events-created").withRel("profile"));

    return ResponseEntity.created(uri).body(new EventResource(event));
//    return ResponseEntity.created(uri).build();
//    return ResponseEntity.created(uri).body(event);

  }

//  @PostMapping
//  public ResponseEntity createEvent(@RequestBody Event event) {
//    final Event newEvent = this.eventRepository.save(event);
//
//    final URI uri
//        = linkTo(methodOn(EventController.class).createEvent(null))
//        .slash(newEvent.getId())
//        .toUri();
//    event.setId(10);
////    return ResponseEntity.created(uri).build();
//    return ResponseEntity.created(uri).body(event);
//  }


  @GetMapping
  public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler assembler) {
    final Page<Event> page = this.eventRepository.findAll(pageable);
    final PagedModel<Event> pagedModel = assembler.toModel(page);
    pagedModel.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
    return ResponseEntity.ok(pagedModel);
  }

  @GetMapping
  public ResponseEntity getEvents(@PathVariable Integer id, Pageable pageable,
                                  PagedResourcesAssembler<Event> assembler,
                                  @CurrentUser Account account) { // 바로 주입받음


//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    User principal = (User) authentication.getPrincipal();

    final Page<Event> page = this.eventRepository.findAll(pageable);
    final PagedModel<EventResource> pageResources = assembler.toModel(page, e -> new EventResource(e));
    pageResources.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));

    if (account != null) {
      pageResources.add(linkTo(EventController.class).withRel("create-event"));
    }

    return ResponseEntity.ok(pageResources);
  }

  @GetMapping("/{id}")
  public ResponseEntity getEvent(@PathVariable Integer id,
                                 @CurrentUser Account currentUser) {

    // 문자열로 나옴 -> 코딩이 필요 @CurrentUser
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


    final Optional<Event> optionalEvent = this.eventRepository.findById(id);
    if (optionalEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    final Event event = optionalEvent.get();
    final EventResource eventResource = new EventResource(event);
    eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));

    if (event.getManager().equals(currentUser)) {
      eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event "));
    }
    return ResponseEntity.ok(eventResource);
  }

  @PutMapping("/{id}")
  public ResponseEntity updateEvent(@PathVariable Integer id,
                                    @RequestBody @Valid EventDto eventDto,
                                    Errors errors,
                                    @CurrentUser Account currentUser) {
    final Optional<Event> optionalEvent = this.eventRepository.findById(id);
    if (optionalEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    if (errors.hasErrors()) {
      return badRequest(errors);
    }

    this.eventValidator.validate(eventDto, errors);
    if (errors.hasErrors()) {
      return badRequest(errors);
    }

    final Event event = optionalEvent.get();
    if (!event.getManager().equals(currentUser)) {
      return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    this.modelMapper.map(eventDto, event);    // eventDto -> event (src->dest)
    final Event save = this.eventRepository.save(event);// 수정 내용 저장

    EventResource eventResource = new EventResource(save);
    eventResource.add(new Link("/docs/index.html#resources-events-update").withRel("profile"));

    return ResponseEntity.ok(eventResource);


  }


  private ResponseEntity<ErrorsResource> badRequest(Errors errors) {
    return ResponseEntity.badRequest().body(new ErrorsResource(errors));
  }
}
