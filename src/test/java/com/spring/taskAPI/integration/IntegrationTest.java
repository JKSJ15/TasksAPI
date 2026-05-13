package com.spring.taskAPI.integration;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.spring.taskAPI.configurations.JwtService;
import com.spring.taskAPI.dto.LoginDto;
import com.spring.taskAPI.dto.TaskDto;
import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.repository.TaskRepository;
import com.spring.taskAPI.repository.UserRepository;
import com.spring.taskAPI.util.TaskUtilTest;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTest {
	@Autowired
	ObjectMapper mapper;
	@Autowired
	MockMvc mvc;
	@Autowired
	UserRepository ur;
	@Autowired
	JwtService jwt;
	@Autowired
	TaskRepository tr;
	@Autowired
	PasswordEncoder encoder;

	User user;
	TaskDto dto;
	LoginDto login;
	String token;
	String taskJson;
	String loginJson;

	@BeforeEach
	void setup() throws Exception {
		tr.deleteAll();
		ur.deleteAll();

		String rawPassword = "123";
		user = new User("teste@teste", encoder.encode(rawPassword));
		login = new LoginDto(user.getLogin(), rawPassword);
		ur.save(user);
		token = jwt.generateToken(user);

		dto = TaskUtilTest.returnTaskDto();

		taskJson = mapper.writeValueAsString(dto);
		loginJson = mapper.writeValueAsString(login);
	}

	// REGISTER
	@Test
	@DisplayName("register_ReturnCREATED_WhenSucessful")
	public void register_ReturnCREATED_WhenSucessful() throws Exception {
		String newUser = """
				{
				  "login":"newlogin@new",
				  "password":"123456"
				 }
				 """;
		mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(newUser))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("register_ReturnBAD_REQUEST_WhenUserAlreadyExist")
	public void register_ReturnBAD_REQUEST_WhenUserAlreadyExist() throws Exception {
		mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(loginJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("register_ReturnBAD_REQUEST_WhenNoContent")
	public void register_ReturnBAD_REQUEST_WhenNoContent() throws Exception {
		mvc.perform(post("/auth/login")).andExpect(status().isBadRequest());
	}

	// LOGIN
	@Test
	@DisplayName("login_ReturnOK_WhenSucessful")
	public void login_ReturnOK_WhenSucessful() throws Exception {
		mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(loginJson))
				.andExpect(status().isOk()).andExpect(content().string(not(emptyString())));
	}

	@Test
	@DisplayName("login_ReturnBAD_REQUEST_WhenNoContent")
	public void login_ReturnBAD_REQUEST_WhenNoContent() throws Exception {
		mvc.perform(post("/auth/login")).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("login_ReturnUNAUTHORIZED_WhenUserNoExists")
	public void login_ReturnUNAUTHORIZED_WhenUserNoExists() throws Exception {
		String newUser = """
				{
				  "login":"newlogin@new",
				  "password":"123456"
				 }
				 """;
		mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(newUser))
				.andExpect(status().isUnauthorized());
	}

	// GET
	@Test
	@DisplayName("get_ReturnOK_WhenSucessful")
	public void get_ReturnOK_WhenSucessful() throws Exception {
		mvc.perform(get("/tasks").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("get_ReturnUNAUTHORIZED_WhenNoToken")
	public void get_ReturnUNAUTHORIZED_WhenNoToken() throws Exception {
		mvc.perform(get("/tasks")).andExpect(status().isUnauthorized());
	}

	// GET BY ID
	@Test
	@DisplayName("getById_ReturnOK_WhenSucessful")
	public void getById_ReturnOK_WhenSucessful() throws Exception {
		long id = createTask();
		mvc.perform(get("/tasks/{id}", id).header("Authorization", "Bearer " + token)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("getById_ReturnNOT_FOUND_WhenNoFound")
	public void getById_ReturnNOT_FOUND_WhenNoFound() throws Exception {
		mvc.perform(get("/tasks/{id}", 89898989l).header("Authorization", "Bearer " + token))
				.andExpect(status().isNotFound());
	}

	// POST
	@Test
	@DisplayName("post_ReturnCREATED_WhenSucessful")
	public void post_ReturnCREATED_WhenSucessful() throws Exception {
		mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(taskJson).header("Authorization",
				"Bearer " + token)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value(dto.getTitle()));
	}

	@Test
	@DisplayName("post_ReturnUNAUTHORIZED_WhenNoToken")
	public void post_ReturnUNAUTHORIZED_WhenNoToken() throws Exception {
		mvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON).content(taskJson))
				.andExpect(status().isUnauthorized());
	}

	// DELETE
	@Test
	@DisplayName("delete_ReturnNO_CONTENT_WhenSucessful")
	public void delete_ReturnNO_CONTENT_WhenSucessful() throws Exception {
		long id = createTask();

		mvc.perform(delete("/tasks/{$id}", id).header("Authorization", "Bearer " + token))
				.andExpect(status().isNoContent());

		mvc.perform(get("/tasks/{id}", id).header("Authorization", "Bearer " + token)).andExpect(status().isNotFound());

	}

	@Test
	@DisplayName("delete_ReturnUNAUTHORIZED_WhenNoToken")
	public void delete_ReturnUNAUTHORIZED_WhenNoToken() throws Exception {
		long id = createTask();

		mvc.perform(delete("/tasks/{$id}", id)).andExpect(status().isUnauthorized());

		mvc.perform(get("/tasks/{id}", id)).andExpect(status().isUnauthorized());

	}

	// UPDATE
	@Test
	@DisplayName("update_ReturnOK_WhenSucessful")
	public void update_ReturnOK_WhenSucessful() throws Exception {

		long id = createTask();
		dto.setId(id);

		taskJson = mapper.writeValueAsString(dto);

		mvc.perform(put("/tasks/{id}", id).contentType(MediaType.APPLICATION_JSON).content(taskJson)
				.header("Authorization", "Bearer " + token)).andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(dto.getTitle()));
	}

	@Test
	@DisplayName("update_ReturnUNAUTHORIZED_WhenNoToken")
	public void update_ReturnUNAUTHORIZED_WhenNoToken() throws Exception {

		long id = createTask();
		dto.setId(id);

		taskJson = mapper.writeValueAsString(dto);

		mvc.perform(put("/tasks/{id}", id).content(taskJson)).andExpect(status().isUnauthorized());
	}

	// METHOD
	private long createTask() throws Exception {
		String response = mvc
				.perform(post("/tasks").header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(taskJson))
				.andReturn().getResponse().getContentAsString();

		Number id = JsonPath.read(response, "$.id");
		return id.longValue();
	}
}
