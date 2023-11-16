package com.lrh.blog.chathandler.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.blog.chathandler.service.BlogGroupService;
import com.lrh.blog.chathandler.service.BlogUserFriendsService;
import com.lrh.blog.chathandler.utils.RedisUtils;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.entity.BlogGroup;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.utils.RedisPrefix;
import com.lrh.blog.common.utils.RedisPrefixUtils;
import com.lrh.blog.common.vo.BlogUserFriendsVo;
import com.lrh.blog.common.vo.GroupUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.controller
 * @ClassName: RedisMessageController
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/9 10:35
 */
@RestController
@RequestMapping("/blog/redis")
public class RedisMessageController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private BlogUserFriendsService blogUserFriendsService;

    @Autowired
    private BlogGroupService blogGroupService;

    @GetMapping("/get/apply/{userId}")
    public Result<Map<Object, Object>> getApply(@PathVariable Long userId) {
        Map<Object, Object> apply = redisUtils.hmget("add" + userId);
        if (apply != null) {
            Map<Object, Object> result = new HashMap<>();
            result.put("apply", apply);
            result.put("applySize", apply.size());
            return Result.ok(result);
        }
        return Result.ok();
    }

    @GetMapping("/getFriendSingleMessage/{userId}/{friendId}")
    public Result<List<Object>> getSingleMessage(@PathVariable("userId") String userId, @PathVariable("friendId") String friendId) {
        StringBuilder onLineString = RedisPrefixUtils.getStringBuilder(userId, friendId, RedisPrefix.SINGLE_CHAT_ONLINE);
        StringBuilder offLineString = RedisPrefixUtils.getStringBuilder(userId, friendId, RedisPrefix.SINGLE_CHAT_OFFLINE);
        List<Object> onLine = redisUtils.lGet(onLineString.toString(), 0, -1);
        List<Object> offLine = redisUtils.lGet(offLineString.toString(), 0, -1);
        onLine.addAll(offLine);

        for (Object object : offLine) {
            Message message = (Message) object;
            if (Objects.equals(message.getToId(), userId)) {
                redisUtils.lSet(onLineString.toString(), object);
                redisUtils.lRemove(offLineString.toString(), 0, object);
            }
        }

        return Result.ok(onLine);
    }

    @GetMapping("/getOffline/{userId}")
    public Result<Map<Long, Integer>> getOffline(@PathVariable("userId") String userId) {

        List<BlogUsers> friendList = blogUserFriendsService.getFriendList(Long.valueOf(userId));
        List<Long> friendListId = new ArrayList<>();
        for (BlogUsers friend : friendList) {
            friendListId.add(friend.getUserId());
        }
        Map<Long, Integer> result = new HashMap<>();

        for (Long id : friendListId) {
            StringBuilder offLineString = RedisPrefixUtils.getStringBuilder(userId, String.valueOf(id), RedisPrefix.SINGLE_CHAT_OFFLINE);
            List<Object> objects = redisUtils.lGet(offLineString.toString(), 0, -1);
            for (Object object : objects) {
                Message message = (Message) object;
                if (Objects.equals(message.getToId(), userId)) {
                    result.put(id, objects.size());
                }
            }
        }
        return Result.ok(result);
    }

    @GetMapping("/redPoint/add/{userId}/{friendId}")
    public Result<Map<Long, Integer>> addRedPoint(@PathVariable("userId") String userId, @PathVariable("friendId") String friendId) {
        StringBuilder stringBuilder = RedisPrefixUtils.getStringBuilder(userId, friendId, RedisPrefix.RED_POINT);
        if (!redisUtils.hHasKey(stringBuilder.toString(), friendId + "-" + userId)) {
            redisUtils.hset(stringBuilder.toString(), friendId + "-" + userId, 1);
        } else {
            Integer redPoint = (Integer) redisUtils.hget(stringBuilder.toString(), friendId + "-" + userId);
            int add = redPoint + 1;
            redisUtils.hset(stringBuilder.toString(), friendId + '-' + userId, add);
        }

        List<BlogUsers> friendList = blogUserFriendsService.getFriendList(Long.valueOf(userId));
        List<Long> friendListId = new ArrayList<>();
        for (BlogUsers friend : friendList) {
            friendListId.add(friend.getUserId());
        }
        Map<Long, Integer> result = new HashMap<>();

        for (Long id : friendListId) {
            StringBuilder redPoint = RedisPrefixUtils.getStringBuilder(userId, String.valueOf(id), RedisPrefix.RED_POINT);
            Object object = redisUtils.hget(redPoint.toString(), id + "-" + userId);
            result.put(id, (Integer) object);
        }
        return Result.ok(result);
    }

    @GetMapping("/redPoint/exit/{userId}")
    public Result<Map<Long, Integer>> exitRedPoint(@PathVariable("userId") String userId) {
        List<BlogUsers> friendList = blogUserFriendsService.getFriendList(Long.valueOf(userId));
        List<Long> friendListId = new ArrayList<>();
        for (BlogUsers friend : friendList) {
            friendListId.add(friend.getUserId());
        }
        Map<Long, Integer> result = new HashMap<>();

        for (Long id : friendListId) {
            StringBuilder redPoint = RedisPrefixUtils.getStringBuilder(userId, String.valueOf(id), RedisPrefix.RED_POINT);
            Object object = redisUtils.hget(redPoint.toString(), id + "-" + userId);
            result.put(id, (Integer) object);
        }
        return Result.ok(result);
    }

    @PutMapping("/redPoint/clear")
    public Result<String> clearRedPoint(@RequestBody BlogUserFriendsVo blogUserFriendsVo) {
        StringBuilder stringBuilder = RedisPrefixUtils.getStringBuilder(String.valueOf(blogUserFriendsVo.getUserId()), String.valueOf(blogUserFriendsVo.getFriendId()), RedisPrefix.RED_POINT);
        redisUtils.del(stringBuilder.toString());
        return Result.ok("clear");
    }

    @PutMapping("/redPoint/group/clear")
    public Result<String> clearGroupRedPoint(@RequestBody GroupUserVo groupUserVo) {
        redisUtils.del(RedisPrefix.RED_POINT + groupUserVo.getUserId() + "-group:" + groupUserVo.getGroupId());
        return Result.ok("clear");
    }


    @GetMapping("/redPoint/group/add/{userId}/{groupId}")
    public Result<Map<Long, Integer>> addGroupRedPoint(@PathVariable("userId") String userId, @PathVariable("groupId") String groupId) {
        String string = RedisPrefix.RED_POINT + userId + "-group:" + groupId;
        if (!redisUtils.hasKey(string)) {
            redisUtils.set(string, 1);
        } else {
            redisUtils.incr(string, 1);
        }
        List<BlogGroup> groupList = blogGroupService.list(new LambdaQueryWrapper<BlogGroup>().eq(BlogGroup::getUserId, userId));
        Map<Long, Integer> result = new HashMap<>();
        for (BlogGroup blogGroup : groupList) {
            if (redisUtils.get(RedisPrefix.RED_POINT + userId + "-group:" + blogGroup.getGroupId()) != null) {
                Object object = redisUtils.get(RedisPrefix.RED_POINT + userId + "-group:" + blogGroup.getGroupId());
                result.put(blogGroup.getGroupId(), Integer.valueOf(object.toString()));
            } else {
                result.put(blogGroup.getGroupId(), 0);
            }
        }
        return Result.ok(result);
    }


    @GetMapping("/redPoint/group/exit/{userId}")
    public Result<Map<Long, Integer>> exitGroupRedPoint(@PathVariable("userId") String userId) {
        List<BlogGroup> groupList = blogGroupService.list(new LambdaQueryWrapper<BlogGroup>().eq(BlogGroup::getUserId, userId));
        Map<Long, Integer> result = new HashMap<>();
        for (BlogGroup blogGroup : groupList) {
            if (redisUtils.get(RedisPrefix.RED_POINT + userId + "-group:" + blogGroup.getGroupId()) != null) {
                Object object = redisUtils.get(RedisPrefix.RED_POINT + userId + "-group:" + blogGroup.getGroupId());
                result.put(blogGroup.getGroupId(), Integer.valueOf(object.toString()));
            } else {
                result.put(blogGroup.getGroupId(), 0);
            }
        }
        return Result.ok(result);
    }

    @GetMapping("/allGroupMessage/{groupId}")
    public Result<List<Message>> allGroupMessage(@PathVariable("groupId") String groupId) {
        List<Object> objects = redisUtils.lGet(RedisPrefixUtils.groupHistoryMessagePrefix(groupId), 0, -1);
        Map<Object, Object> hmget = redisUtils.hmget(RedisPrefixUtils.groupMessagePrefix(groupId));
        List<Message> messageList = new ArrayList<>();
        for (Map.Entry<Object, Object> map : hmget.entrySet()) {
            messageList.add((Message) map.getValue());
        }
        for (Object object : objects) {
            Message message = (Message) object;
            messageList.add(message);
        }
        messageList.sort(Comparator.comparing(Message::getTime));
        return Result.ok(messageList);
    }


    @PostMapping("/group/setBitmap/{groupId}/{userId}")
    public Result<String> groupOffLineClear(@PathVariable("groupId") String groupId, @PathVariable("userId") String userId) {
        int count = blogGroupService.count(new LambdaQueryWrapper<BlogGroup>().eq(BlogGroup::getGroupId, groupId));
        Object hget = redisUtils.hget(RedisPrefixUtils.groupPrefix(groupId), RedisPrefixUtils.groupUserIdPrefix(userId));
        Map<Object, Object> hmget = redisUtils.hmget(RedisPrefixUtils.groupMessagePrefix(groupId));
        for (Map.Entry<Object, Object> map : hmget.entrySet()) {
            redisUtils.setBit(map.getKey(), Long.parseLong(hget.toString()), true);
            if (count == redisUtils.bitCount((String) map.getKey())) {
                redisUtils.del((String) map.getKey());
                redisUtils.hdel(RedisPrefixUtils.groupMessagePrefix(groupId), map.getKey());
                redisUtils.lSet(RedisPrefixUtils.groupHistoryMessagePrefix(groupId), map.getValue());
            }
        }
        return Result.ok("bitMap sets");
    }

}
