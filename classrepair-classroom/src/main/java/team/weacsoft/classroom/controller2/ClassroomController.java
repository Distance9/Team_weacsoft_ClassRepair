package team.weacsoft.classroom.controller2;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.weacsoft.classroom.service.ClassroomService2;
import team.weacsoft.classroom.service.IClassroomService;
import team.weacsoft.common.exception.handler.ApiResp;

import javax.validation.constraints.NotNull;

/**
 * 课室管理
 * @author GreenHatHG
 * @since 2020-01-27
 */
@Validated
@RestController
@RequestMapping(value="/api/v2")
public class ClassroomController {

    private IClassroomService classroomService;
    @Autowired
    private ClassroomService2 classroomService2;

    @Autowired
    public ClassroomController(IClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    /**
     * 获取课室信息
     * @return
     */
    @GetMapping("/public/classrooms")
    public ResponseEntity<ApiResp> getClassRooms(){
        return ApiResp.ok(classroomService.getClassRooms());
    }
    /**
     * 获取课室信息，第二版
     * @return
     */
    @GetMapping("/public/classrooms/m2")
    public ResponseEntity<ApiResp> getClassRooms2(){
        return ApiResp.ok(classroomService2.getClassrooms());
    }

    /**
     * 根据id获取某一课室
     * @param id
     * @return
     */
    @GetMapping("/public/classrooms/classid")
    public ResponseEntity<ApiResp> getClassRoomById(@NotNull Integer id){
        return ApiResp.ok(classroomService.getClassroomById(id));
    }

    @GetMapping("/public/classrooms/State")
    public ResponseEntity<ApiResp> getClassRoomState(Integer id){
        return ApiResp.ok(null);
    }
}
