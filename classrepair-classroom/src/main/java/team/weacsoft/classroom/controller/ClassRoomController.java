package team.weacsoft.classroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.weacsoft.classroom.service.ClassroomService;
import team.weacsoft.common.exception.handler.ApiResp;


/**
 * @menu 课室管理
 * @author GreenHatHG
 **/

@RestController
public class ClassRoomController {

    @Autowired
    private ClassroomService classroomService;

    //todo 优化：不必每次都生成，可以查缓存

    /**
     * 获取课室信息
     * @return
     */
    @GetMapping("/classrooms")
    public ResponseEntity<ApiResp> getClassRooms(){
        return ApiResp.ok(classroomService.getClassRooms());
    }

}
