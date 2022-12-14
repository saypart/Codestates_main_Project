package seb40.main023.server.restdocs.review;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import seb40.main023.server.review.controller.ReviewController;
import seb40.main023.server.review.dto.ReviewPatchDto;
import seb40.main023.server.review.dto.ReviewPostDto;
import seb40.main023.server.review.dto.ReviewResponseDto;
import seb40.main023.server.review.entity.Review;
import seb40.main023.server.review.mapper.ReviewMapper;
import seb40.main023.server.review.service.ReviewService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static Singleton.server.util.ApiDocumentUtils.getRequestPreProcessor;
import static Singleton.server.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ReviewControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewMapper reviewMapper;

    @Autowired
    private Gson gson;

    private ReviewResponseDto responseDto;


    @BeforeEach
    public void init() throws Exception{
        long reviewId = 1L;
        ReviewResponseDto responseDto = new ReviewResponseDto();
        responseDto.setReviewId(1L);
        responseDto.setMemberId(1L);
        responseDto.setReviewBody("??????");


        String content = gson.toJson(responseDto);
        this.responseDto = responseDto;
    }

    @Test
    public void postReviewTest() throws Exception {
        // given
        ReviewPostDto post = ReviewPostDto.builder()
                .reviewId(1L)
                .memberId(1L)
                .reviewBody("??????")
                .build();

        String content = gson.toJson(post);

        given(reviewMapper.reviewPostDtoToReview(Mockito.any(ReviewPostDto.class))).willReturn(Mockito.mock(Review.class));
        given(reviewService.createReview(Mockito.any(Review.class))).willReturn(Mockito.mock(Review.class));
        given(reviewMapper.reviewToReviewResponseDto(Mockito.any(Review.class))).willReturn(responseDto);
        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/v1/review")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.reviewId").value(post.getReviewId()))
                .andExpect(jsonPath("$.data.memberId").value(post.getMemberId()))
                .andExpect(jsonPath("$.data.reviewBody").value(post.getReviewBody()))
                .andDo(document("post-review",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("reviewId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("reviewBody").type(JsonFieldType.STRING).description("????????????")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.reviewId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data.reviewBody").type(JsonFieldType.STRING).description("????????????")

                                )
                        )
                ));
    }

    @Test
    public void patchReviewTest() throws Exception {
        // given
        long reviewId = 1L;
        ReviewPatchDto patch = ReviewPatchDto.builder()
                .reviewId(reviewId)
                .reviewBody("??????")
                .build();

        String content = gson.toJson(patch);

        given(reviewMapper.reviewPatchDtoToReview(Mockito.any(ReviewPatchDto.class))).willReturn(Mockito.mock(Review.class));
        given(reviewService.updateReview(Mockito.any(Review.class))).willReturn(Mockito.mock(Review.class));
        given(reviewMapper.reviewToReviewResponseDto(Mockito.any(Review.class))).willReturn(responseDto);
        // when
        ResultActions actions =
                mockMvc.perform(
                        patch("/v1/review/{review-Id}",1L,reviewId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.reviewId").value(patch.getReviewId()))
                .andExpect(jsonPath("$.data.reviewBody").value(patch.getReviewBody()))
                .andDo(document("patch-review",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("reviewId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("reviewBody").type(JsonFieldType.STRING).description("????????????")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.reviewId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data.reviewBody").type(JsonFieldType.STRING).description("????????????")

                                )
                        )
                ));

    }

    @Test
    public void getReviewTest() throws Exception {
        // given
        long reviewId = 1L;

        given(reviewService.findReview(reviewId)).willReturn(Mockito.mock(Review.class));
        given(reviewMapper.reviewToReviewResponseDto(Mockito.any(Review.class))).willReturn(responseDto);
        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/v1/review/{review-Id}",1L,reviewId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content)
                );
        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-review",
//                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
//                        requestFields(
//                                List.of(
//                                        fieldWithPath("reviewId").type(JsonFieldType.NUMBER).description("????????????"),
//                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("?????????"),
//                                        fieldWithPath("reviewBody").type(JsonFieldType.STRING).description("????????????")
//                                )
//                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.reviewId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data.reviewBody").type(JsonFieldType.STRING).description("????????????")

                                )
                        )
                ));
    }

    @Test
    public void getReviewsTest() throws Exception {
        // given
        int page = 1;
        int size = 10;

        Review review1 = new Review();
        review1.setReviewId(1L);
        review1.setMember(null);
        review1.setReviewBody("??????1");

        Review review2 = new Review();
        review1.setReviewId(2L);
        review1.setMember(null);
        review1.setReviewBody("??????2");

        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);

        Page<Review> pageReview = new PageImpl<>(reviews);

        ReviewResponseDto responseDto1 = new ReviewResponseDto();
        responseDto1.setReviewId(1L);
        responseDto1.setMemberId(1L);
        responseDto1.setReviewBody("??????1");

        ReviewResponseDto responseDto2 = new ReviewResponseDto();
        responseDto2.setReviewId(2L);
        responseDto2.setMemberId(2L);
        responseDto2.setReviewBody("??????2");

        List<ReviewResponseDto> reviewResponseDtos = List.of(responseDto1, responseDto2);

        given(reviewService.findReviews(Mockito.anyInt(),Mockito.anyInt())).willReturn(pageReview);
        given(reviewMapper.reviewToReviewResponseDtos(Mockito.any(List.class))).willReturn(reviewResponseDtos);
        // when
        ResultActions actions =
                mockMvc.perform(
                        get("/v1/review?page={page}&size={size}",1,10,page,size)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("get-reviews",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("page").description("?????? ?????????"),
                                parameterWithName("size").description("???????????? ??????")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                        fieldWithPath("data[].reviewId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data[].reviewBody").type(JsonFieldType.STRING).description("????????????"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("???????????? ??????"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("??? ????????????")
                                )
                        )
                ));
    }

    @Test
    public void deleteReviewTest() throws Exception {
        // given
        long reviewId = 1L;
        // when
        ResultActions actions =
                mockMvc.perform(
                        delete("/v1/review/{review-id}",1L,reviewId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON));
        // then
        MvcResult result = actions.andExpect(status().isNoContent())
                .andDo(
                        document("delete-review")).andReturn();
    }
}
