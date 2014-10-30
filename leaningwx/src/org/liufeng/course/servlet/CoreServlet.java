package org.liufeng.course.servlet;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.liufeng.course.service.CoreService;
import org.liufeng.course.util.SignUtil;
/**
* �������ĺ�����
*
* @author liufeng
* @date 2013-09-01
*/
public class CoreServlet extends HttpServlet {
private static final long serialVersionUID = 4440739483644821986L;
/**
* ����У�飨ȷ����������΢�ŷ�������
*/
public void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
	// ΢�ż���ǩ��
	String signature = request.getParameter("signature");
	// ʱ���
	String timestamp = request.getParameter("timestamp");
	// �����
	String nonce = request.getParameter("nonce");
	// ����ַ���
	String echostr = request.getParameter("echostr");
	PrintWriter out = response.getWriter();
	// ����У�飬��У��ɹ���ԭ������echostr����ʾ����ɹ����������ʧ��
	if (SignUtil.checkSignature(signature, timestamp, nonce)) {
		out.print(echostr);
	}
	out.close();
	out = null;
}
/**
* ����΢�ŷ�������������Ϣ
*/
public void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
	// ��������Ӧ�ı��������ΪUTF-8����ֹ�������룩
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
//			response.setContentType("application/json; charset=utf-8"); 
			

			// ���ú���ҵ���������Ϣ��������Ϣ
			String respMessage = CoreService.processRequest(request);
			
			// ��Ӧ��Ϣ
			PrintWriter out = response.getWriter();
			out.print(respMessage);
			
			out.close();
}
}