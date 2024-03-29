/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

@SpringBootApplication
@RestController
@EnableOAuth2Client

public class SocialApplication extends WebSecurityConfigurerAdapter {

	@Autowired
	OAuth2ClientContext oAuth2ClientContext;

	@RequestMapping({ "/user", "/me" })
	public Map<String, String> user(Principal principal) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("name", principal.getName());
//		OAuth2RestTemplate vkTemplate = new OAuth2RestTemplate(vk(), oAuth2ClientContext);
//		UserInfoTokenServices tokenServicesvk = new UserInfoTokenServices(vkResource().getUserInfoUri(), vk().getClientId());
//		tokenServicesvk.setRestTemplate(vkTemplate);
//
//		FriendResponse response = vkTemplate.getForObject("https://api.vk.com/method/friends.get?v=5.59", );
//		List<VkontakteFriend> friends = response.getItems();
//		System.out.println(principal.getName());
//		System.out.println("*****************************************************************************");
//		for (VkontakteFriend friend:friends){
//			System.out.println(friend.getId()+friend.getFirstName()+friend.getLastName());
//		}
//		System.out.println("*****************************************************************************");
//
//

		return map;
	}



	//TODO как это оформить на фронтенде? И еще я так думаю можно было не воротить сушности френдреспонс и френд
    //	а воспользоваться  json парсером и не париться
//	@RequestMapping("/friend")
//	public Map<String,String> friends() {
//		OAuth2RestTemplate vkTemplate = new OAuth2RestTemplate(vk(), oAuth2ClientContext);
//		UserInfoTokenServices tokenServicesvk = new UserInfoTokenServices(vkResource().getUserInfoUri(), vk().getClientId());
//		tokenServicesvk.setRestTemplate(vkTemplate);
//
//		FriendResponse response = vkTemplate.getForObject("https://api.vk.com/method/friends.get?v=5.59", FriendResponse.class);
//		List<VkontakteFriend> friends = response.getItems();
//		Map<String, String> map = new LinkedHashMap<>();
//		for (VkontakteFriend friend : friends) {
//
//			map.put("name", friend.getFirstName());
//			map.put("lastname", friend.getLastName());
//			map.put("id",String.valueOf(friend.getId()));
////			??????????
//		}
//
//		return map;
//	}

	@RequestMapping("/friends")
	public Map<String,String> friends() {
//		https://vk.com/dev/friends.get?params[user_id]=6492&params[order]=name&params[count]=3&params[offset]=5&params[fields]=city%2Cdomain&params[name_case]=ins&params[v]=5.95
				OAuth2RestTemplate vkTemplate = new OAuth2RestTemplate(vk(), oAuth2ClientContext);
		UserInfoTokenServicesForVk tokenServicesvk = new UserInfoTokenServicesForVk("https://api.vk.com/method/friends.get?fields=city,domain&count=10&v=5.59", vk().getClientId());
		tokenServicesvk.setRestTemplate(vkTemplate);

		ObjectNode response = vkTemplate.getForObject("https://api.vk.com/method/friends.get?fields=city,domain&count=10&v=5.59", ObjectNode.class);
		ArrayNode data = (ArrayNode) response.get("response").get("items");
		Map<String, String> map = new LinkedHashMap<>();

		for (JsonNode dataNode : data) {
			map.put("id",dataNode.get("id").asText());
			map.put("first_name",dataNode.get("first_name").asText());
			map.put("last_name",dataNode.get("last_name").asText());
		}

		return map;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.antMatcher("/**").authorizeRequests().antMatchers("/", "/login**", "/webjars/**", "/error**").permitAll().anyRequest()
				.authenticated().and().exceptionHandling()
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")).and().logout()
				.logoutSuccessUrl("/").permitAll().and().csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
				.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);

	}



	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}
	@Bean
	@ConfigurationProperties("vkontakte.client")
	public AuthorizationCodeResourceDetails vk() {
		return new AuthorizationCodeResourceDetails();
	}
	@Bean
	@ConfigurationProperties("vkontakte.resource")
	public ResourceServerProperties vkResource() {
		return new ResourceServerProperties();
	}

	@Bean
	@ConfigurationProperties("github.client")
	public AuthorizationCodeResourceDetails google()
	{
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("github.resource")
	public ResourceServerProperties googleResource()
	{
		return new ResourceServerProperties();
	}


private Filter ssoFilter()

{   CompositeFilter filter = new CompositeFilter();
	List<Filter> filters = new ArrayList<>();

	OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/github");
	OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
	googleFilter.setRestTemplate(googleTemplate);
	UserInfoTokenServices tokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
	tokenServices.setRestTemplate(googleTemplate);
	googleFilter.setTokenServices(tokenServices);
	filters.add(googleFilter);

	VkCustomFilter vkFilter = new VkCustomFilter("/login/vkontakte");
	OAuth2RestTemplate vkTemplate = new OAuth2RestTemplate(vk(), oAuth2ClientContext);
	vkFilter.setRestTemplate(vkTemplate);
	UserInfoTokenServicesForVk tokenServicesvk = new UserInfoTokenServicesForVk(vkResource().getUserInfoUri(), vk().getClientId());
	tokenServicesvk.setRestTemplate(vkTemplate);
	vkFilter.setTokenServices(tokenServicesvk);
	filters.add(vkFilter);

	filter.setFilters(filters);

	return filter;
}
}

