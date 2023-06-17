package com.tiger.job.server.service.impl;

import com.tiger.job.common.constant.Constant;
import com.tiger.job.common.entity.LoginBody;
import com.tiger.job.common.entity.SysUser;
import com.tiger.job.common.exception.member.ErrorException;
import com.tiger.job.common.exception.member.NotAuthException;
import com.tiger.job.server.mapper.UserMapper;
import com.tiger.job.server.service.UserService;
import com.tiger.job.common.util.CacheUtil;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.common.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author StudiousTiger
 **/
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final CacheUtil cacheUtil;

    public UserServiceImpl(UserMapper userMapper, CacheUtil cacheUtil) {
        this.userMapper = userMapper;
        this.cacheUtil = cacheUtil;
    }

    @Override
    public List<SysUser> selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }
    @Override
    public Object validateLogin(LoginBody body) {
        /* 验证用户名和密码 */
        List<SysUser>  sysUser = userMapper.selectUserByUserName(body.getUserName());
        if (sysUser.size() > 1) {
            throw new NotAuthException("存在多个用户");
        }
        SysUser user = sysUser.get(0);
        if (MeUtil.isEmpty(sysUser) || !user.getPassword().equals(MeUtil.md5(body.getPassword()))) {
            throw new NotAuthException("用户名或密码错误");
        }
        /* 生成token */
        String userId = String.valueOf(user.getId());
        String userName = user.getUserName();
        String accessToken = TokenUtil.createToken(MeUtil.uuid(),userId, Constant.LIVE_TIME);
        String refreshToken = TokenUtil.createToken(MeUtil.uuid(),userId, Constant.LIVE_TIME + 6000000L);
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("userId", userId);
        tokenMap.put("userName", userName);
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);
        cacheUtil.set(userId,accessToken,Constant.LIVE_TIME);
        return tokenMap;
    }

    @Override
    public void logout(String id) {
        try {
            cacheUtil.del(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException("退出操作失败");
        }
    }

    @Override
    public SysUser  selectUserById(String id) {
        return userMapper.selectUserById(id);
    }

    @Override
    public boolean registerUser(SysUser user) {
        return userMapper.registerUser(user);
    }

    @Override
    public int updateUser(SysUser user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int updatePassword(Map<String,String> map) {
        map.put("oldPassword",MeUtil.md5(map.get("oldPassword")));
        map.put("newPassword",MeUtil.md5(map.get("newPassword")));
        return userMapper.updatePassword(map);
    }

    @Override
    public int deleteUserById(String id) {
        return userMapper.deleteUserById(id);
    }
}
