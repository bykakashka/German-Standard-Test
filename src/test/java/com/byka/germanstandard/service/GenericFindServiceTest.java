package com.byka.germanstandard.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GenericFindServiceTest {
    @InjectMocks
    private GenericFindService classUnderTest;

    @Mock
    private EntityManager entityManager;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(classUnderTest, "defaultScale", 5);
    }

    @Test
    public void calculateCTR() {
        assertEquals(0.33333, classUnderTest.calculateCTR(1L, 3L), 0.0000001);
        assertEquals(0, classUnderTest.calculateCTR(0L, 3L), 0.0000001);
        assertEquals(0, classUnderTest.calculateCTR(1L, 0L), 0.0000001);
    }

}