package io.ssosso.rest.index;


import io.ssosso.rest.common.BaseControllerTest;
import io.ssosso.rest.common.RestDocsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test") // application-test.properties -> /test/resources에 만들어서 설
public class IndexControllerTests extends BaseControllerTest {

  @Test
  public void test() throws Exception{
    // /api/events 로 주기를 기대
    this.mockMvc.perform(get("/api"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("_links.events").exists());
  }
}
