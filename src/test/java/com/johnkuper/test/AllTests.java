package com.johnkuper.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PaymentParserTest.class, DBSaverTest.class })
public class AllTests {

}
