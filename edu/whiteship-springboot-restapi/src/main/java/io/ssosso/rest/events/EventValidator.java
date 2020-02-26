package io.ssosso.rest.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {

  public void validate(EventDto eventDto, Errors errors) {
    if (eventDto.getBasePrice() > eventDto.getMaxPrice()
        && eventDto.getMaxPrice() != 0) {
//      errors.rejectValue("basePrice", "wrongValue", "BasePrice is Wrong");
//      errors.rejectValue("maxPrice", "wrongValue", "BasePrice is Wrong");
      errors.reject("wrongPrices", "Value for Prices are Wrong");
    }

    @NotNull final LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
    if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime())
        || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
        || endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())
    ) {
      errors.rejectValue("endEventDatetime", "wrongValue", "endEventDateTime is wrong");
    }

    // TODO BeginEventDateTime
    // TODO CloseEnrollmentDateTime
  }
}
