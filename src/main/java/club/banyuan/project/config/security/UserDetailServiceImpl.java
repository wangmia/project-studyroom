package club.banyuan.project.config.security;


import club.banyuan.project.common.mapper.UserMapper;
import club.banyuan.project.common.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Qualifier("qaUserDetailService")
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<club.banyuan.project.common.model.User> users = userMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(users)) {
            throw new UsernameNotFoundException("用户不存在!"); // 用户名或密码错误
        }
        club.banyuan.project.common.model.User u = users.get(0);

        List<GrantedAuthority> authorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER"); // u.getRoles()
        return new User(u.getUsername(), u.getPassword(), authorities);
    }
}
