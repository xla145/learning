package site.xulian.learning.base;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ErrorTextRender extends Render {
	protected static final String contentType = "text/plain; charset=" + getEncoding();
	
	protected int errorCode;
	protected String errorText;
	
	public ErrorTextRender(int errorCode, String errorText) {
		this.errorCode = errorCode;
		this.errorText = errorText;
	}

	@Override
	public void render() {
		try {
//			response.setStatus(getErrorCode(), URLEncoder.encode(errorText, "utf-8"));
			response.setStatus(getErrorCode());
			response.sendError(getErrorCode(), URLEncoder.encode(errorText, "utf-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		PrintWriter writer = null;
		try {
			response.setHeader("Pragma", "no-cache");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expires", 0);
			response.setContentType(contentType);
	        writer = response.getWriter();
	        writer.write(errorText);
	        writer.flush();
		} catch (IOException e) {
			throw new RenderException(e);
		}
		finally {
			if (writer != null)
				writer.close();
		}
	}
	
	public int getErrorCode() {
		return errorCode;
	}
}
