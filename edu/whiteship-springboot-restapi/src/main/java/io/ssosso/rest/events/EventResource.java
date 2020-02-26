package io.ssosso.rest.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

// bean serialize
//public class EventResource extends RepresentationModel {  // ResourceSupport -> RepresentationModel

//  @JsonUnwrapped
//  private Event event;
//

//  public EventResource(Event event) {
//    this.event = event;
//  }
//
//  public Event getEvent() {
//    return event;
//  }
//}


public class EventResource extends EntityModel<Event> {  // Resource -> EntityModel


  public EventResource(Event content, Link... links) {
    super(content, links);
//    add(new Link("http://localhost:8080/api/events/" + content.getId()));
    add(linkTo(EventController.class).slash(content.getId()).withSelfRel());
  }
}