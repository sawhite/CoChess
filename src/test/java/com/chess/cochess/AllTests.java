package com.chess.cochess;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;
import org.junit.platform.runner.JUnitPlatform;

@RunWith(JUnitPlatform.class)
@SelectPackages({
        "com.chess.cochess",
        "com.chess.cochess.engine",
        "com.chess.cochess.gui"
})
public class AllTests {
}
