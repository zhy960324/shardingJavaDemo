package com.example.demo.config.shardingconfig;

import org.apache.shardingsphere.orchestration.reg.api.RegistryCenter;
import org.apache.shardingsphere.orchestration.reg.api.RegistryCenterConfiguration;
import org.apache.shardingsphere.orchestration.reg.listener.DataChangedEventListener;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistryCenter implements RegistryCenter {
	public static Map<String, DataChangedEventListener> listeners = new ConcurrentHashMap<>();
	
	private RegistryCenterConfiguration config;
	
	private Properties properties;
	/**
	 * public 是为了在重置节点的时候减少去重新读配置
	 */
	public static Map<String, String> values = new ConcurrentHashMap<>();
	
	@Override
	public void init(RegistryCenterConfiguration config) {
		this.config = config;
	}
	
	@Override
	public String get(String key) {
		return values.get(key);
	}
	
	@Override
	public String getDirectly(String key) {
		return values.get(key);
	}
	
	@Override
	public boolean isExisted(String key) {
		return values.containsKey(key);
	}
	
	@Override
	public List<String> getChildrenKeys(String key) {
		return null;
	}
	
	@Override
	public void persist(String key, String value) {
		values.put(key, value);
	}
	
	@Override
	public void update(String key, String value) {
		values.put(key, value);
	}
	
	@Override
	public void persistEphemeral(String key, String value) {
		values.put(key, value);
	}
	
	@Override
	public void watch(String key, DataChangedEventListener dataChangedEventListener) {
		if (null != dataChangedEventListener) {
			// 将数据改变的事件监听器缓存下来
			listeners.put(key, dataChangedEventListener);
		}
	}
	
	@Override
	public void close() {
		config = null;
	}
	
	@Override
	public void initLock(String key) {
		
	}
	
	@Override
	public boolean tryLock() {
		return false;
	}
	
	@Override
	public void tryRelease() {
		
	}
	
	@Override
	public String getType() {
		// 【关键点1】，留着文章后续引用
		return "shardingLocalRegisterCenter";
	}
	
	@Override
	public Properties getProperties() {
		return properties;
	}
	
	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}