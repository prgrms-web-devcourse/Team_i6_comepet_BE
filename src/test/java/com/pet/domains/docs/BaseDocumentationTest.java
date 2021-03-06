package com.pet.domains.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.common.config.SecurityConfig;
import com.pet.common.jwt.JwtAuthentication;
import com.pet.common.property.JwtProperty;
import com.pet.common.property.RefreshJwtProperty;
import com.pet.domains.account.controller.AccountController;
import com.pet.domains.account.controller.NotificationController;
import com.pet.domains.account.service.AccountService;
import com.pet.domains.account.service.LoginService;
import com.pet.domains.account.service.NotificationService;
import com.pet.domains.animal.controller.AnimalController;
import com.pet.domains.animal.service.AnimalService;
import com.pet.domains.area.controller.CityController;
import com.pet.domains.area.service.CityService;
import com.pet.domains.auth.service.AuthenticationService;
import com.pet.domains.comment.controller.CommentController;
import com.pet.domains.comment.service.CommentService;
import com.pet.domains.docs.controller.CommonDocumentationController;
import com.pet.domains.image.service.ImageService;
import com.pet.domains.post.controller.MissingPostController;
import com.pet.domains.post.controller.ShelterPostController;
import com.pet.domains.post.service.MissingPostBookmarkService;
import com.pet.domains.post.service.MissingPostService;
import com.pet.domains.post.service.ShelterPostBookmarkService;
import com.pet.domains.post.service.ShelterPostService;
import com.pet.domains.statistics.controller.PostStatisticsController;
import com.pet.domains.statistics.service.PostStatisticsService;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = {
    CommonDocumentationController.class,
    AccountController.class,
    NotificationController.class,
    AnimalController.class,
    CityController.class,
    CommentController.class,
    MissingPostController.class,
    ShelterPostController.class,
    PostStatisticsController.class},
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    }
)
@AutoConfigureRestDocs
@EnableConfigurationProperties(value = {JwtProperty.class, RefreshJwtProperty.class})
@Disabled
public abstract class BaseDocumentationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected AuthenticationService authenticationService;

    @MockBean
    protected ImageService imageService;

    @MockBean
    protected MissingPostService missingPostService;

    @MockBean
    protected AnimalService animalService;

    @MockBean
    protected ShelterPostBookmarkService shelterPostBookmarkService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected ShelterPostService shelterPostService;

    @MockBean
    protected MissingPostBookmarkService missingPostBookmarkService;

    @MockBean
    protected PostStatisticsService postStatisticsService;

    @MockBean
    protected NotificationService notificationService;

    @MockBean
    protected CityService cityService;

    @MockBean
    protected LoginService loginService;

    protected JwtAuthentication getAuthenticationToken() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
