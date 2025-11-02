package com.example.steplang;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

public class TokenCleanupListener extends AbstractTestExecutionListener {
    /*@Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        MockMvc mockMvc = testContext.getApplicationContext().getBean(MockMvc.class);
        TestUserCreation.init(mockMvc);
    }
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        MockMvc mockMvc = testContext.getApplicationContext().getBean(MockMvc.class);
        TestUserCreation.init(mockMvc);
    }*/
    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        //System.out.println("afterTest Method");
        //TestUserCreation.clearToken();

        //MockMvc mockMvc = testContext.getApplicationContext().getBean(MockMvc.class);

        //TestUserCreation.init(mockMvc);

    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
       // System.out.println("afterTest Class");
        //TestUserCreation.clearToken();

        //MockMvc mockMvc = testContext.getApplicationContext().getBean(MockMvc.class);
       // TestUserCreation.init(mockMvc);
    }
}
