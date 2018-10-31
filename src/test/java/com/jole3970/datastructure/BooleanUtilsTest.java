package com.jole3970.datastructure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class BooleanUtilsTest {

    private static Stream<Arguments> intArrayAndExpectedBoolArray() {
        return Stream.of(
                arguments(new int[]{1}, new boolean[]{true}),
                arguments(new int[]{0}, new boolean[]{false}),
                arguments(new int[]{-1}, new boolean[]{true}),
                arguments(new int[]{-2}, new boolean[]{false}),
                arguments(new int[]{1, 2}, new boolean[]{true, false})
        );
    }
    @ParameterizedTest
    @MethodSource("intArrayAndExpectedBoolArray")
    void shouldConvertToBitsCorrectly(int[] intArr, boolean[] expected) {
        boolean[] result = BooleanUtils.boolArrayFromIntArray(intArr);
        assertThat(result, is(expected));
    }


    private static Stream<Arguments> boolArrayAndExpectedIntArray() {
        return Stream.of(
                arguments(new boolean[]{true}, new int[]{1}),
                arguments(new boolean[]{false}, new int[]{0}),
                arguments(new boolean[]{true, false}, new int[]{1, 0})
        );
    }
    @ParameterizedTest
    @MethodSource("boolArrayAndExpectedIntArray")
    void shouldConvertFromBitsCorrectly(boolean[] intArr, int[] expected) {
        int[] result = BooleanUtils.intArrayFromBoolArray(intArr);
        assertThat(result, is(expected));
    }
}
