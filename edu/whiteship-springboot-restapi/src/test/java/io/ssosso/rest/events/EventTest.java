package io.ssosso.rest.events;

import com.example.demo.events.Event;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

  @Test
  public void builder() {
    Event event
        = Event.builder()
        .name("Inflearn Spring REST API")
        .description("REST API development with Spring")
        .build();

    assertThat(event).isNotNull();
  }

  @Test
  public void javaBean() {

    // Given
    Event event = new Event();
    final String name = "Event";
    final String description = "Spring REST";

    // When
    event.setName(name);
    event.setDescription(description);

    // Then
    assertThat(event.getName()).isEqualTo(name);
    assertThat(event.getDescription()).isEqualTo(description);
  }

  @Test
//  @Parameters({
//      "0, 0, true",
//      "100, 0, false",
//      "0, 100, false",
//      "0, 100, true"
//  })
  @Parameters//(method = "paramsForTestFree")
  public void testFree(int basePrice, int maxPrice, boolean isFree) {

    // Given
    Event event
        = Event.builder()
        .basePrice(basePrice)
        .maxPrice(maxPrice)
        .build();

    // When
    event.update();

    // Then
    assertThat(event.isFree()).isEqualTo(isFree);
  }

  private Object[] parametersForTestFree() {
    return new Object[]{
        new Object[]{0, 0, true},
        new Object[]{100, 0, false},
        new Object[]{0, 1000, false},
        new Object[]{100, 200, false}
    };
  }

  @Test
  @Parameters
  public void testOffline(String location, boolean isOffline) {
    // Given
    Event event
        = Event.builder()
        .location(location)
        .build();

    // When
    event.update();

    // Then
    assertThat(event.isOffline()).isEqualTo(isOffline);

  }

  private Object[] parametersForTestOffline() {
    return new Object[]{
        new Object[]{"강남", true},
        new Object[]{null, false},
        new Object[]{"  ", false}
    };
  }
}