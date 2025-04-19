package raisetech.StudentManagement2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.StudentManagement2.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の検索が実行できて空で帰ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の登録が実行できて空で帰ってくること()throws Exception {
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                    "student":{
                        "name":"上嶋二郎",
                        "kanaName":"ウワジマジロウ",
                        "nickname":"ジロウ",
                        "email":"test@example.com",
                        "area":"宮崎",
                        "age":3,
                        "sex":"男性",
                        "remark":null,
                        "isDeleted":false
                    },
                    "studentCourseList":[
                      {
                        "courseName":"AWSコース",
                        "courseStatus": {
                        "status": "仮申込"
                        }
                      }
                    ]
                }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新が実行できて空で帰ってくること()throws Exception {
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
        """
            {
                "student":{
                    "id": 7,
                    "name": "テストさん",
                    "kanaName": "テストさん",
                    "nickname": "テストさん",
                    "email": "test@example.com",
                    "area": "愛知県",
                    "age": 38,
                    "sex": "男性",
                    "remark": "なし",
                    "deleted": false
                },
                "studentCourseList":[
                {
                     "id": 13,
                     "studentId": 7,
                     "courseName": "AWSコース",
                     "courseStartAt": "2025-04-02T15:26:53",
                     "courseEndAt": "2026-04-02T15:26:53",
                     "courseStatus": {
                       "status": "受講中"
                     }
                   }
              ]
            }
            """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void コースステータスの一覧取得が実行できて空のリストが返ること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/courseStatusList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchCourseStatuses();
  }

  @Test
  void コースステータスの登録が実行できること() throws Exception {
    mockMvc.perform(post("/registerCourseStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                  "studentCourseId": 1,
                  "status": "仮申込"
              }
              """))
        .andExpect(status().isOk())
        .andExpect(content().string("ステータスを登録しました。"));

    verify(service, times(1)).registerCourseStatus(any());
  }

  @Test
  void コースステータスの更新が実行できること() throws Exception {
    mockMvc.perform(put("/updateCourseStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                  "id": 5,
                  "studentCourseId": 1,
                  "status": "受講中"
              }
              """))
        .andExpect(status().isOk())
        .andExpect(content().string("ステータスを更新しました。"));

    verify(service, times(1)).updateCourseStatus(any());
  }

  @Test
  void コースステータスの削除が実行できること() throws Exception {
    int id = 5;
    mockMvc.perform(MockMvcRequestBuilders.delete("/deleteCourseStatus/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().string("ステータスを削除しました。"));

    verify(service, times(1)).deleteCourseStatus(id);
  }

  @Test
  void 検索条件に基づいた受講生詳細の検索が実行できて空のリストが返ること() throws Exception {
    mockMvc.perform(post("/searchStudentDetails")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                  "name": "山田",
                  "area": "東京",
                  "ageFrom": 20,
                  "ageTo": 30,
                  "courseName": "Java",
                  "status": "受講中"
              }
              """))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentDetailsByCondition(any());
  }
}