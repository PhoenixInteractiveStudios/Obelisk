module obelisk.commons {
    requires org.jetbrains.annotations;
    requires java.logging;
    requires com.google.gson;

    exports org.burrow_studios.obelisk.commons.crypto;
    exports org.burrow_studios.obelisk.commons.function;
    exports org.burrow_studios.obelisk.commons.logging;
    exports org.burrow_studios.obelisk.commons.turtle;
    exports org.burrow_studios.obelisk.commons.util;
    exports org.burrow_studios.obelisk.commons.util.validation;
}