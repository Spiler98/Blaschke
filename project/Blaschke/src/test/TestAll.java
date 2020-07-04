package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.model.complex.TestBlaschke;
import test.model.complex.TestComplex;
import test.model.util.TestMathUtil;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestComplex.class,
        TestBlaschke.class,
        TestMathUtil.class
})
public class TestAll {

}
