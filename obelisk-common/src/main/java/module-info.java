module obelisk.common {
    requires org.jetbrains.annotations;
    requires java.logging;
    requires com.google.gson;

    exports org.burrow_studios.obelisk.common.crypto;
    exports org.burrow_studios.obelisk.common.function;
    exports org.burrow_studios.obelisk.common.logging;
    exports org.burrow_studios.obelisk.common.turtle;
    exports org.burrow_studios.obelisk.common.util;
    exports org.burrow_studios.obelisk.common.util.validation;
}