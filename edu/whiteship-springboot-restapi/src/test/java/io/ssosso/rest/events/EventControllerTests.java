package io.ssosso.rest.events;

import com.example.demo.accounts.Account;
import com.example.demo.accounts.AccountRole;
import com.example.demo.accounts.AccountService;
import com.example.demo.common.BaseControllerTest;
import com.example.demo.common.TestDescription;
import com.example.demo.events.Event;
import com.example.demo.events.EventDto;
import com.example.demo.events.EventRepository;
import com.example.demo.events.EventStatus;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest // 웹 용 Bean 등록, Repository 관련 빈등록 안함
public class EventControllerTests extends BaseControllerTest {

//  @MockBean

  @Autowired
  EventRepository eventRepository;

  @Autowired
  AccountService accountService;

  @Test
  @TestDescription("정상적으로 이벤트를 생성하는 테스트")
  public void createEvent() throws Exception {
    EventDto event
        = EventDto.builder()
        .name("Spring")
        .description("REST API Development with Spring")
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
        .basePrice(100)
        .maxPrice(200)
        .limitOfEnrollment(100)
        .location("강남역 D2 스타트업 팩토리")
        .build();

//    event.setId(10);
//    Mockito.when(eventRepository.save(event)).thenReturn(event);

    this.mockMvc
        .perform(
            post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, "Bearer"+getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").exists())
        .andExpect(header().exists(HttpHeaders.LOCATION))
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
        .andExpect(jsonPath("free").value(false))
        .andExpect(jsonPath("offline").value(true))
        .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        .andExpect(jsonPath("_links.self").exists())
        .andExpect(jsonPath("_links.query-events").exists())
        .andExpect(jsonPath("_links.update-event").exists())
        .andDo(document("create-event",// 문서의 이름
            links(
                linkWithRel("self").description("link to self"),
                linkWithRel("query-events").description("link to query events"),
                linkWithRel("update-event").description("link to update")
            ),
            requestHeaders(
                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                headerWithName(HttpHeaders.CONTENT_TYPE).description("content ")
            ),
            requestFields(
                fieldWithPath("name").description("Name of Event"),
                fieldWithPath("description").description("description of new event"),
                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                fieldWithPath("endEventDateTime").description("date time of close of new event"),
                fieldWithPath("location").description("date time of close of new event"),
                fieldWithPath("basePrice").description("date time of close of new event"),
                fieldWithPath("maxPrice").description("date time of close of new event"),
                fieldWithPath("limitOfEnrollment").description("date time of close of new event")
            ),
            responseHeaders(
                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
            ),
            relaxedResponseFields(
                fieldWithPath("id").description("ID of Event"),
                fieldWithPath("name").description("Name of Event"),
                fieldWithPath("description").description("description of new event"),
                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                fieldWithPath("endEventDateTime").description("date time of close of new event"),
                fieldWithPath("location").description("date time of close of new event"),
                fieldWithPath("basePrice").description("date time of close of new event"),
                fieldWithPath("maxPrice").description("date time of close of new event"),
                fieldWithPath("limitOfEnrollment").description("date time of close of new event"),
                fieldWithPath("free").description("date time of close of new event"),
                fieldWithPath("offline").description("date time of close of new event"),
                fieldWithPath("eventStatus").description("date time of close of new event")
            )
        ))
    ;

  }


  @Test
  @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
  public void create_bad_request() throws Exception {
    Event event
        = Event.builder()
        .name("Spring")
        .description("REST API Development with Spring")
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
        .basePrice(100)
        .maxPrice(200)
        .limitOfEnrollment(100)
        .location("강남역 D2 스타트업 팩토리")
        .free(true)
        .offline(false)
        .eventStatus(EventStatus.PUBLISHED)
        .build();

//    event.setId(10);
//    Mockito.when(eventRepository.save(event)).thenReturn(event);

    mockMvc
        .perform(
            post("/api/events/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer"+getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("id").exists())
        .andExpect(header().exists(HttpHeaders.LOCATION))
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
        .andExpect(jsonPath("id").value(Matchers.not(100)))
        .andExpect(jsonPath("free").value(Matchers.not(true)))
        .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT))
    ;

  }

  private String getAccessToken() throws Exception {
    // Given
    final String username = "ssosso.dev@gmail.com";
    final String password = "sso";
    final Account sso = Account.builder()
        .email(username)
        .password(password)
        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
        .build();

    this.accountService.saveAccount(sso);

    String clientId = "myApp";
    String clientSecret = "pass";

    final ResultActions perform = this.mockMvc.perform(post("/oauth/token")
        .with(httpBasic(clientId, clientSecret))
        .param("username", username)
        .param("password", password)
        .param("grant_type", "password")  // 인증타입 -> password 인증
    );

    final MockHttpServletResponse response = perform.andReturn().getResponse();
    final String responseBody = response.getContentAsString();
    Jackson2JsonParser jsonParser = new Jackson2JsonParser();
    return jsonParser.parseMap(responseBody).get("access_token").toString();
  }

  @Test
  @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
  public void createEvent_Bad_Request_Empty_Input() throws Exception {
    EventDto eventDto
        = EventDto.builder()
        .name("Spring")
        .description("REST API Development with Spring")
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
        .basePrice(10000)
        .maxPrice(200)
        .limitOfEnrollment(100)
        .location("강남역 D2 스타트업 팩토리")
        .build();

    this.mockMvc
        .perform(
            post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
  public void createEvent_Bad_Request_Wrong_Input() throws Exception {
    EventDto eventDto
        = EventDto.builder()
        .name("Spring")
        .description("REST API Development with Spring")
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
        .basePrice(10000)
        .maxPrice(200)
        .limitOfEnrollment(100)
        .location("강남역 D2 스타트업 팩토리")
        .build();

    this.mockMvc
        .perform(
            post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].objectName").exists())
        .andExpect(jsonPath("$[0].defaultMessage").exists())
        .andExpect(jsonPath("$[0].code").exists())
//        .andExpect(jsonPath("$[0].field").exists())
//        .andExpect(jsonPath("$[0].rejectedValue").exists())

    ;
  }


  @Test
  @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
  public void test() throws Exception {
    // Given
    IntStream.range(0, 30).forEach(i -> {
      this.generateEvent(i);
    });

    // When
    this.mockMvc
        .perform(
            get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
        )
        // Then
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("page").exists())
    ;

  }

  @Test
  @TestDescription("기존의 이벤트를 하나 조회하기")
  public void getEvent() throws Exception {
    // Given
    final Event event = this.generateEvent(100);

    // When
    this.mockMvc
        .perform(get("/api/events/{id}", event.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").exists())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("_links.self").exists())
        .andExpect(jsonPath("_links.profile").exists());

  }

  @Test
  @TestDescription("없는 이벤트는 조회 했을때 404 응답 받기")
  public void getEvent404() throws Exception {

    this.mockMvc
        .perform(get("/api/events/118883"))
        .andExpect(status().isNotFound());

  }


  @Test
  @TestDescription("이벤트를 정상적으로 수정하기")
  public void updateEvent() throws Exception {

    // Given
    final Event event = this.generateEvent(200);
    String eventName = "Update Event";
    EventDto eventDto = this.modelMapper.map(event, EventDto.class);
    eventDto.setName(eventName);

    // When & Then
    this.mockMvc
        .perform(put("/api/events/{id}", event.getId())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value(eventName))
        .andExpect(jsonPath("_links.self").exists())
        .andDo(document("update-event"))  // 모든 테스트 문서화 해야됨
    ;
  }

  @Test
  @TestDescription("입력값이 비어있는 경우에 이벤트 수정 실패")
  public void updateEvent400_Empty() throws Exception {

    // Given
    final Event event = this.generateEvent(200);
    EventDto eventDto = this.modelMapper.map(event, EventDto.class);

    // When & Then
    this.mockMvc
        .perform(put("/api/events/{id}", event.getId())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패")
  public void updateEvent400_Wrong() throws Exception {

    // Given
    final Event event = this.generateEvent(200);
    EventDto eventDto = this.modelMapper.map(event, EventDto.class);
    eventDto.setBasePrice(20000);
    eventDto.setMaxPrice(1000);

    // When & Then
    this.mockMvc
        .perform(put("/api/events/{id}", event.getId())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @TestDescription("존재하지 않는 경우에 이벤트 수정 실패")
  public void updateEvent404() throws Exception {

    // Given
    final Event event = this.generateEvent(200);
    EventDto eventDto = this.modelMapper.map(event, EventDto.class);

    // When & Then
    this.mockMvc
        .perform(put("/api/events/1111123131")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(eventDto))
        )
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  private Event generateEvent(int index) {
    Event event = Event.builder()
        .name("event" + index)
        .description("test event")
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
        .basePrice(100)
        .maxPrice(200)
        .limitOfEnrollment(100)
        .location("강남역 D2 스타트업 팩토리")
        .free(false)
        .offline(true)
        .eventStatus(EventStatus.DRAFT)
        .build();

    return this.eventRepository.save(event);
  }


}
