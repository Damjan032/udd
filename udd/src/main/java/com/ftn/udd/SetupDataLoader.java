package com.ftn.udd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.udd.model.Job;
import com.ftn.udd.model.User;
import com.ftn.udd.repositories.IJobRepository;
import com.ftn.udd.repositories.IUserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IJobRepository jobRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup) {
			return;
		}

		User user1 = new User("hr1@gmail.com", passwordEncoder.encode("sifra123"), "Jelena", "Cupac");
		User user2 = new User("hr2@gmail.com", passwordEncoder.encode("sifra123"), "Jelena2", "Cupac2");

		//Job job1 = new Job("Senior Backend Engineer (Node.JS)", "From autonomous industrial robots to self-charging drones and chatbot platforms, we're building it and you can be part of it. Motius is a research and development company specializing in the development of products and prototypes in the latest tech fields (AI, IoT, AR/VR and many more). With the unique combination of a permanent core team and an interdisciplinary tech community, we reinvent R&D and develop the tech products of the future together with our customers. Motius is based in Munich & Stuttgart, Germany and in Dubai. In 2022, we're opening up a new office & location in Serbia.");
		
		//Job job2 = new Job("Java developer", "We are looking for Java Developers with experience in building Java web applications. This includes anything between groups of back-end services and their client (desktop, mobile) counterparts.\r\nYour primary responsibility will be to design and develop these applications, and to coordinate with the rest of the team working on different layers of the infrastructure. Thus, a commitment to collaborative problem solving, sophisticated design, and product quality are essential. \r\nFurthermore, we would like to work with colleagues who are eager to learn and constantly expand their knowledge and expertise.");
				
		//Job job3 = new Job("Senio software developer", "We are looking for an experienced Software Developer to join our team! Happiest Baby is a company based in the States, with the main office being in Los Angeles, that has developed the most advanced smart bassinet in the world called SNOO. We are on the lookout for new talent to join our European regional center in Belgrade! What we need right now is a passionate Software Developer with great understanding of software development concepts and interests for integrating IoT devices.");
		
		Job job1 = new Job("NodeJS Backend Engineer", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
		
		Job job2 = new Job("Java developer", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
				
		Job job3 = new Job("Senior software developer", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
		
		
		
		userRepository.save(user1);
		userRepository.save(user2);
		
		jobRepository.save(job1);
		jobRepository.save(job2);
		jobRepository.save(job3);
		
		alreadySetup = true;
	}

}
