package org.hy.xsso.common;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.net.ClientSocket;
import org.hy.common.net.ClientSocketCluster;
import org.hy.common.xml.XJava;





/**
 * 应用服务的集群操作
 *
 * @author      ZhengWei(HY)
 * @createDate  2017-01-25
 * @version     v1.0
 */
public class AppCluster extends Cluster
{
    
    private AppCluster()
    {
        
    }
    
    
    
    /**
     * 用户首次登陆时，集群同步单点登陆信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2017-01-23
     * @version     v1.0
     *
     * @param i_SessionID   会话ID
     * @param i_SessionData 会话数据（一般为登陆的用户信息）
     */
    public static void loginCluster(String i_SessionID ,Object i_SessionData)
    {
        List<ClientSocket> v_Servers = getAppServers();
        
        if ( !Help.isNull(v_Servers) )
        {
            ClientSocketCluster.sendObjects(v_Servers 
                                           ,getClusterTimeout() 
                                           ,i_SessionID
                                           ,i_SessionData 
                                           ,getSSOSessionTimeOut());
        }
    }
    
    
    
    /**
     * 用户退出时，集群移除单点登陆信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2017-01-23
     * @version     v1.0
     *
     * @param i_SessionID  会话ID
     */
    public static void logoutCluster(String i_SessionID)
    {
        List<ClientSocket> v_Servers = getAppServers();
        
        if ( !Help.isNull(v_Servers) )
        {
            ClientSocketCluster.removeObjects(v_Servers ,getClusterTimeout() ,i_SessionID);
        }
    }
    
    
    
    /**
     * 持会话活力及有效性
     * 
     * @author      ZhengWei(HY)
     * @createDate  2017-02-04
     * @version     v1.0
     *
     * @param i_SessionID          会话ID
     * @param i_SessionData        会话数据（一般为登陆的用户信息）
     * @param i_DataExpireTimeLen  数据的过期时长(单位：秒)。小于等于0或为空，表示永远有效
     */
    public static void aliveCluster(String i_SessionID ,Object i_SessionData ,long i_DataExpireTimeLen)
    {
        List<ClientSocket> v_Servers = getAppServers();
        
        if ( !Help.isNull(v_Servers) )
        {
            ClientSocketCluster.sendObjects(v_Servers 
                                           ,getClusterTimeout() 
                                           ,i_SessionID
                                           ,i_SessionData 
                                           ,i_DataExpireTimeLen);
        }
    }
    
    
    
    /**
     * 获取集群配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2017-01-17
     * @version     v1.0
     *
     * @return
     */
    public static List<ClientSocket> getAppServers()
    {
        String []          v_ClusterServers = Help.NVL(Help.NVL(XJava.getParam("AppServers") ,new Param()).getValue()).split(",");
        List<ClientSocket> v_Clusters       = new ArrayList<ClientSocket>();
        
        if ( !Help.isNull(v_ClusterServers) )
        {
            for (String v_Server : v_ClusterServers)
            {
                if ( !Help.isNull(v_Server) )
                {
                    String [] v_HostPort = (StringHelp.replaceAll(v_Server.trim() ,new String[]{" " ,"\t" ,"\r" ,"\n"} ,new String[]{""}) + ":1721").split(":");
                    
                    v_Clusters.add(new ClientSocket(v_HostPort[0] ,Integer.parseInt(v_HostPort[1])));
                }
            }
        }
        
        return v_Clusters;
    }
    
}
