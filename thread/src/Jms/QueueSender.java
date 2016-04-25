package Jms;
 
import javax.jms.DeliveryMode;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
 
/**
 * <b>function:</b> Queue ��ʽ��Ϣ������
 * @author hoojo
 * @createDate 2013-6-19 ����04:34:36
 * @file QueueSender.java
 * @package com.hoo.mq.queue
 * @project ActiveMQ-5.8
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class QueueSender {
    
    // ���ʹ���
    public static final int SEND_NUM = 5;
    // tcp ��ַ
    public static final String BROKER_URL = "tcp://localhost:61616";
    // Ŀ�꣬��ActiveMQ����Ա����̨���� http://localhost:8161/admin/queues.jsp
    public static final String DESTINATION = "hoo.mq.queue";
    
    /**
     * <b>function:</b> ������Ϣ
     * @author hoojo
     * @createDate 2013-6-19 ����12:05:42
     * @param session
     * @param sender
     * @throws Exception
     */    
    public static void sendMessage(QueueSession session, javax.jms.QueueSender sender) throws Exception {
        for (int i = 0; i < SEND_NUM; i++) {
            String message = "����Map��Ϣ��" + (i + 1) + "��";
            
            MapMessage map = session.createMapMessage();
            map.setString("text", message);
            map.setLong("time", System.currentTimeMillis());
            System.out.println(map);
            
            sender.send(map);
        }
    }
    
    public static void run() throws Exception {
        
        QueueConnection connection = null;
        QueueSession session = null;
        try {
            // �������ӹ���
            QueueConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
            // ͨ����������һ������
            connection = factory.createQueueConnection();
            // ��������
            connection.start();
            // ����һ��session�Ự
            session = connection.createQueueSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // ����һ����Ϣ����
            Queue queue = session.createQueue(DESTINATION);
            // ������Ϣ������
            javax.jms.QueueSender sender = session.createSender(queue);
            // ���ó־û�ģʽ
            sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            sendMessage(session, sender);
            // �ύ�Ự
            session.commit();
            
        } catch (Exception e) {
            throw e;
        } finally {
            // �ر��ͷ���Դ
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        QueueSender.run();
    }
}