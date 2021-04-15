package com.zj.zrpc.netty;

import com.zj.base.constants.CHCCenter;
import com.zj.base.constants.RegisterCenter;
import com.zj.base.entity.RpcRequestEntity;
import com.zj.base.entity.RpcResponseEntity;
import com.zj.base.entity.ServerInfo;
import com.zj.register.core.Send;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterTestHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        RpcRequestEntity rpcRequestEntity = (RpcRequestEntity) obj;
        switch (rpcRequestEntity.getDataType()) {
            case REGISTER:
                ServerInfo serverInfo = (ServerInfo) rpcRequestEntity.getData();
                RegisterCenter.add(serverInfo.getName(), serverInfo);
                log.info("已有服务[{}]", RegisterCenter.getRuList());
               // is.close();
                break;
            case SEND:
                CHCCenter.add(rpcRequestEntity.getRequestID(), ctx);
                log.info("转发远程调用到具体服务器");
                Send invokeSend = new Send();
                invokeSend.send0(rpcRequestEntity);
                log.info("转发远程调用到具体服务器结束");
                break;
            case GET_SERVICE_LIST:
                log.info("获取已注册服务列表");
                RpcResponseEntity rpcResponseEntity = new RpcResponseEntity(RegisterCenter.getRuList());
               // SerializeUtil.send(rpcResponseEntity, ChannelHandlerContext);
                break;
            default:
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new RpcResponseEntity("success"));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
