package team.weacsoft.timetable.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.weacsoft.common.exception.BadRequestException;
import team.weacsoft.common.log.Log;
import team.weacsoft.common.utils.JwtUtil;
import team.weacsoft.timetable.domain.TimeTableDo;
import team.weacsoft.timetable.repository.TimeTableRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GreenHatHG
 */

@Component
public class TimeTableService {

    private TimeTableRepository tableRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public TimeTableService(TimeTableRepository tableRepository, JwtUtil jwtUtil) {
        this.tableRepository = tableRepository;
        this.jwtUtil = jwtUtil;
    }

    @Log(module = "值班", operation = "签到或签退")
    public TimeTableDo signInOrOut(Integer state, HttpServletRequest request){
        String id = jwtUtil.getIdFromHttpServletRequest(request);
        if(state == 2 && tableRepository.findByUserId(id).getState() != 1){
                throw new BadRequestException("该用户还没有签到，请先进行签到");
        }
        return tableRepository.save(new TimeTableDo(id, state));
    }

    /**
     * 返回当前值班中人员
     */
    public List<TimeTableDo> findAllOnline(){
        List<TimeTableDo> list = tableRepository.findAll();
        if(list.size() == 0){
            return null;
        }
        return list.stream()
                .filter(timeTableDo -> timeTableDo.getState() == 1)
                .collect(Collectors.toList());
    }
}