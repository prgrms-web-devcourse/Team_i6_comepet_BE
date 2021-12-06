package com.pet.domains.docs;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.domains.account.controller.AccountController;
import com.pet.domains.account.controller.NotificationController;
import com.pet.domains.animal.controller.AnimalController;
import com.pet.domains.area.controller.CityController;
import com.pet.domains.comment.controller.CommentController;
import com.pet.domains.post.controller.MissingPostController;
import com.pet.domains.post.controller.ShelterPostController;
import com.pet.domains.statistics.controller.PostStatisticsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = {
//    CommonDocumentationController.class,
    AccountController.class,
    NotificationController.class,
    AnimalController.class,
    CityController.class,
    CommentController.class,
    MissingPostController.class,
    ShelterPostController.class,
    PostStatisticsController.class
})
@AutoConfigureRestDocs
public abstract class BaseDocumentationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
