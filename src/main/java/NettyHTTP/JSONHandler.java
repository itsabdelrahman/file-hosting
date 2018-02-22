package NettyHTTP;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.json.JSONObject;

public class JSONHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object incomingPayload) {
        ByteBuf buffer = (ByteBuf) incomingPayload;
        JSONObject incomingJSON = new JSONObject(buffer.toString(CharsetUtil.UTF_8));
        System.out.println(">>> INCOMING HTTP REQUEST PAYLOAD --> " + incomingJSON.toString());

        JSONObject outgoingJSON = new JSONObject();
        outgoingJSON.put("counter", incomingJSON.getInt("counter") + 1);
        System.out.println(">>> OUTGOING HTTP RESPONSE PAYLOAD --> " + outgoingJSON.toString());

        ByteBuf content = Unpooled.copiedBuffer(outgoingJSON.toString(), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
        channelHandlerContext.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
