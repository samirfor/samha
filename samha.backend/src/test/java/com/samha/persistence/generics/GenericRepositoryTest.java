package com.samha.persistence.generics;

import com.samha.domain.Eixo;
import com.samha.persistence.IEixoRepository;
import org.junit.jupiter.api.AfterEach;  // MUDOU de org.junit.After
import org.junit.jupiter.api.BeforeEach; // MUDOU de org.junit.Before
import org.junit.jupiter.api.Test;       // MUDOU de org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.test.context.junit4.SpringRunner; // REMOVIDO (não necessário com JUnit 5)
// import org.junit.runner.RunWith; // REMOVIDO

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// @RunWith(SpringRunner.class) // REMOVIDO - @SpringBootTest já faz isso no JUnit 5
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GenericRepositoryTest {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private IEixoRepository eixoRepository;

    @BeforeEach // MUDOU de @Before
    public void setUp() {
        String principal = "USER_TEST";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach // MUDOU de @After
    public void tearDown() {
        eixoRepository.deleteAll();
    }

    @Test // Annotation correta, mas o import precisava mudar
    public void itShouldGetAllRecordsWithGivenClassType() {
        Eixo eixo = new Eixo("EIXO_TEST_1");
        Eixo eixo1 = new Eixo("EIXO_TEST_2");
        genericRepository.save(eixo);
        genericRepository.save(eixo1);

        List<Eixo> eixos = genericRepository.findAll(Eixo.class);

        assertThat(eixos.size()).isEqualTo(2);
    }

    @Test // Annotation correta, mas o import precisava mudar
    public void itShouldRetrieveAnEntityByClassTypeAndId() {
        Eixo eixo = new Eixo("EIXO_TEST_3");
        eixo = genericRepository.save(eixo);

        eixo = genericRepository.get(Eixo.class, eixo.getId());

        assertThat(eixo).isNotNull();
        assertThat(eixo.getId()).isGreaterThan(0L);
    }
}