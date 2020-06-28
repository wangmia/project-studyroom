package club.banyuan.project.controller.api;

import club.banyuan.project.common.api.CommonResult;

import club.banyuan.project.common.mapper.SeatMapper;
import club.banyuan.project.common.mapper.UserMapper;
import club.banyuan.project.common.model.User;
import club.banyuan.project.common.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author WM
 * @date 2020/6/23 11:10 上午
 * 描述信息：
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private SeatMapper seatMapper;

    @Autowired
    private UserMapper userMapper;

//    // 新建座位
//    @PostMapping(value = "/create")
//    public CommonResult create(@RequestBody Seat seat, HttpSession session) {
//        UserAdmin user = (UserAdmin) session.getAttribute("user");
//        if (user == null) {
//            return CommonResult.failed("您尚未登录!");
//        }
//
//        //            answerService.create(answer, user);
//        return CommonResult.success("OK");
//    }

    @PostMapping(value = "/login")
    public CommonResult login(@RequestBody User user, HttpSession session) {

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(user.getUsername())
                .andPasswordEqualTo(user.getPassword());

        List<User> userList = userMapper.selectByExample(userExample);

        if(CollectionUtils.isEmpty(userList)){
            return CommonResult.failed("用户名或密码不正确");
        }

        User u = userList.get(0);

        session.setAttribute("user", u);

        return CommonResult.success("ok");

//        // 获取用户在不在
//        UserExample example = new UserExample();
//        example.createCriteria().andUsernameEqualTo(user.getUsername());
//        List<User> tmpUsers = userMapper.selectByExample(example);
//        if (CollectionUtils.isEmpty(tmpUsers)) {
//            return CommonResult.failed("用户不存在！");
//        }
//
//        User tmpUser = tmpUsers.get(0);
//
//        // 判断密码对不对
//        if (!tmpUser.getPassword().equals(user.getPassword())) {
//            return CommonResult.failed("密码错误！");
//        }
//
//        // 放进 Session
//        session.setAttribute("user", tmpUser);
//        return CommonResult.success("OK");

    }

    @PostMapping(value = "/register")
    public CommonResult register(@RequestBody User user) {

        // 用户名和密码不能为空
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return CommonResult.failed("注册失败!");
        }

        userMapper.insertSelective(user);
        return CommonResult.success("OK");
    }

}

