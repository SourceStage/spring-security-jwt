package com.example.spring_security_jwt.boostrap;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;

import com.example.spring_security_jwt.constant.RoleEnum;
import com.example.spring_security_jwt.entity.PermissionEntity;
import com.example.spring_security_jwt.entity.RoleEntity;
import com.example.spring_security_jwt.entity.UserEntity;
import com.example.spring_security_jwt.repository.PermissionRepository;
import com.example.spring_security_jwt.repository.RoleRepository;
import com.example.spring_security_jwt.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InitBatch {

	PasswordEncoder passwordEncoder;

	@NonFinal
	static final String ADMIN_USER_NAME = "admin";

	@NonFinal
	static final String ADMIN_PASSWORD = "admin";

	@Bean
	@ConditionalOnProperty(prefix = "spring", value = "datasource.driver-class-name", havingValue = "org.postgresql.Driver")
	ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository,
			PermissionRepository permissionRepository) {
		log.info("Initializing application.....");
		return args -> {
			Optional<UserEntity> userFound = userRepository.findByUsername(ADMIN_USER_NAME);

			if (userFound.isEmpty()) {

				var roleAdmin = RoleEntity.builder().roleName(RoleEnum.ADMIN.name()).roleDescription("User role")
						.build();

				List<PermissionEntity> permissionList = permissionRepository.findAll();
				if (CollectionUtils.isEmpty(permissionList)) {
					var permissionCreate = PermissionEntity.builder().permissionName("create")
							.permissionDescription("create entity").build();
					var permissionUpdate = PermissionEntity.builder().permissionName("update")
							.permissionDescription("update entity").build();
					permissionRepository.save(permissionCreate);
					permissionRepository.save(permissionUpdate);
					permissionList.add(permissionCreate);
					permissionList.add(permissionUpdate);
				}

				roleAdmin.setPermissions(new HashSet<>(permissionList));
				Optional<RoleEntity> roleFound = roleRepository.findByRoleName(RoleEnum.ADMIN.name());
				var roles = new HashSet<RoleEntity>();
				if (roleFound.isEmpty()) {
					roleRepository.save(roleAdmin);
					roles.add(roleAdmin);
				} else {
					roles.add(roleFound.get());
				}

				UserEntity userAdmin = UserEntity.builder().username(ADMIN_USER_NAME).email("admin@gmail.com")
						.password(passwordEncoder.encode(ADMIN_PASSWORD)).roles(roles).build();

				userRepository.save(userAdmin);
				log.warn("admin user has been created with default password: admin, please change it");
			}
			log.info("Application initialization completed .....");
		};
	}
}
