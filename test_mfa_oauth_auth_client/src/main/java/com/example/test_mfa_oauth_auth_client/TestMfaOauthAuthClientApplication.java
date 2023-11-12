package com.example.test_mfa_oauth_auth_client;

import com.example.test_mfa_oauth_auth_client.models.CustomUser;
import com.example.test_mfa_oauth_auth_client.models.Role;
import com.example.test_mfa_oauth_auth_client.repo.CustomUserRepo;
import com.example.test_mfa_oauth_auth_client.repo.RoleRepo;
import com.example.test_mfa_oauth_auth_client.repo.UserRoleRepo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
@EnableJpaRepositories
public class TestMfaOauthAuthClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestMfaOauthAuthClientApplication.class, args);
    }

    @Bean
    CommandLineRunner run(CustomUserRepo customUserRepo, RoleRepo roleRepo, UserRoleRepo userRoleRepo) {
        return args -> {
            Log log = LogFactory.getLog(this.getClass());
            Role rolePublisher = new Role();
            rolePublisher.setRoleName("publisher");
            Role roleWriter = new Role();
            roleWriter.setRoleName("writer");
            Role roleReader = new Role();
            roleReader.setRoleName("reader");
            List<Role> roleList = Stream.of(rolePublisher, roleWriter, roleReader).toList();

            roleRepo.saveAllAndFlush(roleList);

            CustomUser test = createCustomUser("test", null);
            CustomUser publisher = createCustomUser("publisher", Stream.of(rolePublisher).toList());
            CustomUser writer = createCustomUser("writer", Stream.of(roleWriter).toList());
            CustomUser reader = createCustomUser("reader", Stream.of(roleReader).toList());
            CustomUser all = createCustomUser("all", Stream.of(rolePublisher, roleWriter, roleReader).toList());

            customUserRepo.save(test);
            customUserRepo.save(publisher);
            customUserRepo.save(writer);
            customUserRepo.save(reader);
            customUserRepo.save(all);
            log.info("======================");
            customUserRepo.findAll().forEach(
                    custom_user -> {
                        log.info((custom_user));
                        custom_user.getRoleList().forEach(log::info);
                    }
            );
        };
    }

    private CustomUser createCustomUser(String account, List<Role> roleList) {
        CustomUser customUser = new CustomUser();
        customUser.setAccount(account);
        customUser.setPassword(new BCryptPasswordEncoder().encode("123"));
        customUser.setActive("2");
        customUser.setRoleList(roleList);

        return customUser;
    }

}
