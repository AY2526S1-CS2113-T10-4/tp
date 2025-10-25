package modhero.data.nusmods;

import modhero.data.modules.Module;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModuleRetrieverTest {
    private final ModuleRetriever retriever = new ModuleRetriever();

    @Test
    void getModuleWithInvalidCodeYieldsNullOrEmptyFields() {
        assertNull(retriever.getModule("invalid", "XXXX"));
    }

    @Test
    void parseRealModuleSucceeds() {
        Module module = retriever.getModule("2025-2026", "CS2113");
        assertEquals("CS2113", module.getCode());
        assertEquals("Software Engineering & Object-Oriented Programming", module.getName());
        assertEquals(4, module.getMc());
        assertNotNull(module.getPrerequisites());
    }
}