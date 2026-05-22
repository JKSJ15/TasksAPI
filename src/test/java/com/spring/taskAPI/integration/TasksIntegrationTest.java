package com.spring.taskAPI.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.spring.taskAPI.configurations.JwtService;
import com.spring.taskAPI.dto.TaskDto;
import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.repository.UserRepository;
import com.spring.taskAPI.util.TaskUtilTest;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TasksIntegrationTest {

	@Value("${api.security.token.secret}")
	String secretTest;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	MockMvc mvc;

	@Autowired
	UserRepository ur;

	@Autowired
	JwtService jwt;

	@Autowired
	PasswordEncoder encoder;

	User user;
	User anotherUser;

	TaskDto dto;

	String taskJson;
	String invalidTaskJson;

	String accessToken;
	String invalidAccessToken = "invalid-token";
	String expiredAccessToken;

	@BeforeEach
	void setup() throws Exception {

		String rawPassword = "123";

		user = new User("teste@teste", encoder.encode(rawPassword));
		anotherUser = new User("another@teste", encoder.encode(rawPassword));

		ur.save(user);
		ur.save(anotherUser);

		accessToken = jwt.generateToken(user);

		expiredAccessToken = JWT.create().withIssuer("tasksAPI").withSubject(user.getLogin())
				.withExpiresAt(Instant.now().minus(1, ChronoUnit.SECONDS)).sign(Algorithm.HMAC256(secretTest));

		dto = TaskUtilTest.returnTaskDto();

		taskJson = mapper.writeValueAsString(dto);

		invalidTaskJson = """
				{
				  "title": "",
				  "description": "Walk with the Dog",
				  "status": "PENDING",
				  "createdAt": "2026-05-20",
				  "deadline": "2026-05-22",
				  "dateConcl": null
				}
				""";
	}

	// GET ALL
	@Test
	@DisplayName("getAll_ReturnOK_WhenSuccessful")
	public void getAll_ReturnOK_WhenSuccessful() throws Exception {

		mvc.perform(get("/tasks").header("Authorization", "Bearer " + accessToken)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("getAll_ReturnUNAUTHORIZED_WhenNoToken")
	public void getAll_ReturnUNAUTHORIZED_WhenNoToken() throws Exception {

		mvc.perform(get("/tasks")).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("getAll_ReturnUNAUTHORIZED_WhenInvalidToken")
	public void getAll_ReturnUNAUTHORIZED_WhenInvalidToken() throws Exception {

		mvc.perform(get("/tasks").header("Authorization", "Bearer " + invalidAccessToken))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("getAll_ReturnUNAUTHORIZED_WhenExpiredToken")
	public void getAll_ReturnUNAUTHORIZED_WhenExpiredToken() throws Exception {

		mvc.perform(get("/tasks").header("Authorization", "Bearer " + expiredAccessToken))
				.andExpect(status().isUnauthorized());
	}

	// GET BY ID
	@Test
	@DisplayName("getById_ReturnOK_WhenSuccessful")
	public void getById_ReturnOK_WhenSuccessful() throws Exception {

		long id = createTask();

		mvc.perform(get("/tasks/{id}", id).header("Authorization", "Bearer " + accessToken)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("getById_ReturnNOT_FOUND_WhenTaskDoesNotExist")
	public void getById_ReturnNOT_FOUND_WhenTaskDoesNotExist() throws Exception {

		mvc.perform(get("/tasks/{id}", 999L).header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("getById_ReturnUNAUTHORIZED_WhenInvalidToken")
	public void getById_ReturnUNAUTHORIZED_WhenInvalidToken() throws Exception {

		long id = createTask();

		mvc.perform(get("/tasks/{id}", id).header("Authorization", "Bearer " + invalidAccessToken))
				.andExpect(status().isUnauthorized());
	}

	// POST
	@Test
	@DisplayName("create_ReturnCREATED_WhenSuccessful")
	public void create_ReturnCREATED_WhenSuccessful() throws Exception {

		mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(taskJson).header("Authorization",
				"Bearer " + accessToken)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value(dto.getTitle()));
	}

	@Test
	@DisplayName("create_ReturnBAD_REQUEST_WhenInvalidBody")
	public void create_ReturnBAD_REQUEST_WhenInvalidBody() throws Exception {

		mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(invalidTaskJson)
				.header("Authorization", "Bearer " + accessToken)).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("create_ReturnUNAUTHORIZED_WhenNoToken")
	public void create_ReturnUNAUTHORIZED_WhenNoToken() throws Exception {

		mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(taskJson))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("create_ReturnUNAUTHORIZED_WhenInvalidToken")
	public void create_ReturnUNAUTHORIZED_WhenInvalidToken() throws Exception {

		mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(taskJson).header("Authorization",
				"Bearer " + invalidAccessToken)).andExpect(status().isUnauthorized());
	}

	// UPDATE
	@Test
	@DisplayName("update_ReturnOK_WhenSuccessful")
	public void update_ReturnOK_WhenSuccessful() throws Exception {

		long id = createTask();

		dto.setId(id);

		String updatedJson = mapper.writeValueAsString(dto);

		mvc.perform(put("/tasks/{id}", id).contentType(MediaType.APPLICATION_JSON).content(updatedJson)
				.header("Authorization", "Bearer " + accessToken)).andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(dto.getTitle()));
	}

	@Test
	@DisplayName("update_ReturnNOT_FOUND_WhenTaskDoesNotExist")
	public void update_ReturnNOT_FOUND_WhenTaskDoesNotExist() throws Exception {
		dto.setId(999L);

		String json = mapper.writeValueAsString(dto);

		mvc.perform(put("/tasks/{id}", 999L)
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(json)
		        .header("Authorization", "Bearer " + accessToken))
		        .andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("update_ReturnBAD_REQUEST_WhenInvalidBody")
	public void update_ReturnBAD_REQUEST_WhenInvalidBody() throws Exception {

		long id = createTask();

		mvc.perform(put("/tasks/{id}", id).contentType(MediaType.APPLICATION_JSON).content(invalidTaskJson)
				.header("Authorization", "Bearer " + accessToken)).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("update_ReturnUNAUTHORIZED_WhenNoToken")
	public void update_ReturnUNAUTHORIZED_WhenNoToken() throws Exception {

		long id = createTask();

		mvc.perform(put("/tasks/{id}", id).contentType(MediaType.APPLICATION_JSON).content(taskJson))
				.andExpect(status().isUnauthorized());
	}

	// DELETE
	@Test
	@DisplayName("delete_ReturnNO_CONTENT_WhenSuccessful")
	public void delete_ReturnNO_CONTENT_WhenSuccessful() throws Exception {

		long id = createTask();

		mvc.perform(delete("/tasks/{id}", id).header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isNoContent());

		mvc.perform(get("/tasks/{id}", id).header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("delete_ReturnNOT_FOUND_WhenTaskDoesNotExist")
	public void delete_ReturnNOT_FOUND_WhenTaskDoesNotExist() throws Exception {

		mvc.perform(delete("/tasks/{id}", 999L).header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("delete_ReturnUNAUTHORIZED_WhenNoToken")
	public void delete_ReturnUNAUTHORIZED_WhenNoToken() throws Exception {

		long id = createTask();

		mvc.perform(delete("/tasks/{id}", id)).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("delete_ReturnUNAUTHORIZED_WhenInvalidToken")
	public void delete_ReturnUNAUTHORIZED_WhenInvalidToken() throws Exception {

		long id = createTask();

		mvc.perform(delete("/tasks/{id}", id).header("Authorization", "Bearer " + invalidAccessToken))
				.andExpect(status().isUnauthorized());
	}

	// AUXILIARY METHODS
	private long createTask() throws Exception {

		String response = mvc
				.perform(post("/tasks").header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(taskJson))
				.andReturn().getResponse().getContentAsString();

		Number id = JsonPath.read(response, "$.id");

		return id.longValue();
	}
}