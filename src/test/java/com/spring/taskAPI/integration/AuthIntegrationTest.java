package com.spring.taskAPI.integration;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import com.spring.taskAPI.dto.LoginDto;
import com.spring.taskAPI.entity.RefreshToken;
import com.spring.taskAPI.entity.User;
import com.spring.taskAPI.repository.TokenRefreshRepository;
import com.spring.taskAPI.repository.UserRepository;
import com.spring.taskAPI.service.TokenRefreshService;
import com.spring.taskAPI.service.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthIntegrationTest {
	@Value("${api.security.token.secret}")
	String secretTest;
	@Autowired
	ObjectMapper mapper;
	@Autowired
	MockMvc mvc;
	@Autowired
	UserRepository ur;
	@Autowired
	UserService us;
	@Autowired
	JwtService jwt;
	@Autowired
	TokenRefreshRepository refreshTokenRepositoryr;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	TokenRefreshService trs;

	User user;
	User user2;

	String loginJson;
	LoginDto login;

	String accessToken;
	String expiredAccessToken;

	String refreshToken;
	RefreshToken expiredRefreshToken;
	String refreshTokenNoSaved;
	String refreshTokenAnotherUser;

	@BeforeEach
	void setup() throws Exception {
		String rawPassword = "123";
		user = new User("teste@teste", encoder.encode(rawPassword));
		user2 = new User("teste2@teste", encoder.encode(rawPassword));

		login = new LoginDto(user.getLogin(), rawPassword);
		ur.save(user);
		ur.save(user2);

		accessToken = jwt.generateToken(user);
		expiredAccessToken = JWT.create().withExpiresAt(Instant.now().minus(1, ChronoUnit.SECONDS))
				.withIssuer("tasksAPI").withSubject(user.getLogin()).sign(Algorithm.HMAC256(secretTest));

		refreshTokenNoSaved = jwt.generateRefreshToken(user2);
		refreshToken = jwt.generateRefreshToken(user);
		refreshTokenAnotherUser = jwt.generateRefreshToken(user2);
		trs.createRefreshToken(user2, refreshTokenAnotherUser);
		expiredRefreshToken = trs.createRefreshToken(user, refreshToken);

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

	// REFRESH
	@Test
	@DisplayName("refresh_ReturnOK_WhenSucessful")
	public void refresh_ReturnOK_WhenSucessful() throws Exception {
		String body = """
				{
				    "refreshToken": "%s"
				}
				""".formatted(refreshToken);
		mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isOk()).andExpect(content().string(not(emptyString())));
	}

	@Test
	@DisplayName("refresh_ReturnUNAUTHORIZED_WhenInvalidRefreshToken")
	public void refresh_ReturnUNAUTHORIZED_WhenInvalidRefreshToken() throws Exception {
		String body = """
				{
				    "refreshToken": "invalid-token"
				}
				""";
		mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@DisplayName("refresh_ReturnBAD_REQUEST_WhenNoToken")
	public void refresh_ReturnBAD_REQUEST_WhenNoToken() throws Exception {
		mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("refresh_ReturnUNAUTHORIZED_WhenRefreshTokenExpired")
	public void refresh_ReturnUNAUTHORIZED_WhenRefreshTokenExpired() throws Exception {
		expiredRefreshToken.setExpiresAt(Instant.now().minus(1, ChronoUnit.DAYS));
		refreshTokenRepositoryr.save(expiredRefreshToken);
		String body = """
				{
				    "refreshToken": "%s"
				}
				""".formatted(expiredRefreshToken);
		mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("refresh_ReturnUNAUTHORIZED_WhenRefreshTokenNoSaved")
	public void refresh_ReturnUNAUTHORIZED_WhenRefreshTokenNoSaved() throws Exception {
		String body = """
				{
				    "refreshToken": "%s"
				}
				""".formatted(refreshTokenNoSaved);
		mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("refresh_ReturnUNAUTHORIZED_WhenRefreshTokenUserWasDeleted")
	public void refresh_ReturnUNAUTHORIZED_WhenRefreshTokenUserWasDeleted() throws Exception {
		us.deleteUser(user2);
		String body = """
				{
				    "refreshToken": "%s"
				}
				""".formatted(refreshTokenAnotherUser);
		mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("refresh_ReturnUNAUTHORIZED_WhenUseOldRefreshToken")
	public void refresh_ReturnUNAUTHORIZED_WhenUseOldRefreshToken() throws Exception {
		String body = """
				{
				    "refreshToken": "%s"
				}
				""".formatted(refreshToken);
		String response = mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		String newRefreshToken = JsonPath.read(response, "$.refreshToken");
		assertNotEquals(refreshToken, newRefreshToken);

		mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isUnauthorized());
	}

	// ACESS TOKEN
	@Test
	@DisplayName("acess_ReturnUNAUTHORIZED_WhenAccessTokenExpired")
	public void acess_ReturnUNAUTHORIZED_WhenAccessTokenExpired() throws Exception {
		mvc.perform(get("/tasks").header("Authorization", "Bearer " + expiredAccessToken))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("acess_ReturnUNAUTHORIZED_WhenInvalidAccessToken")
	public void acess_ReturnUNAUTHORIZED_WhenInvalidAccessToken() throws Exception {
		mvc.perform(get("/tasks").header("Authorization", "Bearer invalid-token")).andExpect(status().isUnauthorized());
	}
	//LOGOUT
	@Test
	@DisplayName("logout_ReturnNO_CONTENT_WhenSucessful")
	public void logout_ReturnNO_CONTENT_WhenSucessful() throws Exception {
	mvc.perform(post("/auth/logout")
	        .header("Authorization", "Bearer " + accessToken))
	        .andExpect(status().isNoContent());
	}
	@Test
	@DisplayName("logout_ReturnUNAUTHORIZED_WhenInvalidAcessToken")
	public void logout_ReturnUNAUTHORIZED_WhenInvalidAcessToken() throws Exception {
	mvc.perform(post("/auth/logout")
	        .header("Authorization", "Bearer invalid-token"))
	        .andExpect(status().isUnauthorized());
	}
	@Test
	@DisplayName("logout_ReturnUNAUTHORIZED_WhenAcessTokenWasExpired")
	public void logout_ReturnUNAUTHORIZED_WhenAcessTokenWasExpired() throws Exception {
	mvc.perform(post("/auth/logout")
	        .header("Authorization", "Bearer " + expiredAccessToken))
	        .andExpect(status().isUnauthorized());
	}
	@Test
	@DisplayName("logout_ReturnUNAUTHORIZED_WhenNoAcessToken")
	public void logout_ReturnUNAUTHORIZED_WhenNoAcessToken() throws Exception {
	mvc.perform(post("/auth/logout")
	        .header("Authorization", "Bearer "))
	        .andExpect(status().isUnauthorized());
	mvc.perform(post("/auth/logout"))
	        .andExpect(status().isUnauthorized());
	}
}
