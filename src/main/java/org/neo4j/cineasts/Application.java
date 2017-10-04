/*
 * Copyright [2011-2016] "Neo Technology"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */
package org.neo4j.cineasts;

import org.neo4j.ogm.config.ClasspathConfigurationSource;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.web.support.OpenSessionInViewInterceptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableNeo4jRepositories("org.neo4j.cineasts.repository")
@EnableTransactionManagement
@ComponentScan("org.neo4j.cineasts")
public class Application implements WebMvcConfigurer {

	@Bean
	public OpenSessionInViewInterceptor openSessionInViewInterceptor() {
		OpenSessionInViewInterceptor openSessionInViewInterceptor =
				new OpenSessionInViewInterceptor();
		openSessionInViewInterceptor.setSessionFactory(sessionFactory());
		return openSessionInViewInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addWebRequestInterceptor(openSessionInViewInterceptor());
	}

	@Bean
	public SessionFactory sessionFactory() {
		ClasspathConfigurationSource configurationSource = new ClasspathConfigurationSource("/ogm.properties");
		org.neo4j.ogm.config.Configuration.Builder builder = new org.neo4j.ogm.config.Configuration.Builder(configurationSource);
		org.neo4j.ogm.config.Configuration configuration = builder.build();

		return new SessionFactory(configuration,"org.neo4j.cineasts.domain");
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new Neo4jTransactionManager(sessionFactory());
	}
}
