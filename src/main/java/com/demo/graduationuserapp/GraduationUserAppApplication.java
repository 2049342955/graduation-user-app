package com.demo.graduationuserapp;

import com.demo.graduationclient.usr.UserRescourceClient;
import com.demo.puddconfig.mybatis.MapperScannerConfig;
import com.demo.puddconfig.mybatis.MybatisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableEurekaClient
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, MybatisConfig.class, MapperScannerConfig.class})
//@ComponentScan(basePackages = {"com.esudian", "com.pudding"}, excludeFilters={
//		@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value={
//				AliyunStsConfig.class, OssStsResourceHelper.class
//		})
//})
//解决其他jar包中装配的bean无法识别
@ComponentScan(basePackages = {"com.demo"})
public class GraduationUserAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraduationUserAppApplication.class, args);
	}

	@Bean
	public UserRescourceClient UserRescourceClient(){
		return new UserRescourceClient();
	}

	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		return corsConfiguration;
	}

	/**
	 * 跨域过滤器
	 * @return
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig()); // 4
		return new CorsFilter(source);
	}
}
