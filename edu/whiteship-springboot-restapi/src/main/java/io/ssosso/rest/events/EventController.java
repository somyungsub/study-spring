package io.ssosso.rest.events;

import io.ssosso.rest.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
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
                                    Errors errors) {

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

  @GetMapping("/{id}")
  public ResponseEntity getEvent(@PathVariable Integer id) {
    final Optional<Event> optionalEvent = this.eventRepository.findById(id);
    if (optionalEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    final Event event = optionalEvent.get();
    final EventResource eventResource = new EventResource(event);
    eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
    return ResponseEntity.ok(eventResource);
  }

  @PutMapping
  public ResponseEntity updateEvent(@PathVariable Integer id,
                                    @RequestBody @Valid EventDto eventDto,
                                    Errors errors) {
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
