package org.burrow_studios.obelkisk.server.form;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.burrow_studios.obelisk.api.FormTemplateMeta;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FormManager {
    private static final Logger LOG = LoggerFactory.getLogger(FormManager.class);

    private final Obelisk obelisk;
    private final Set<FormAccess> entries;

    public FormManager(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
        this.entries = ConcurrentHashMap.newKeySet();
    }

    public void onLoad(@NotNull JDA jda) {
        List<FormTemplateMeta> templatesMeta = this.obelisk.getFormDAO().listTemplates();

        for (FormTemplateMeta meta : templatesMeta) {
            TextChannel channel = jda.getTextChannelById(meta.accessChannel());

            if (channel == null) {
                LOG.warn("Skipping creation of form access \"{}\" because the access channel does not exist or is not reachable.", meta.identifier());
                continue;
            }

            this.createFormAccess(meta.identifier(), channel);
        }
    }

    private void createFormAccess(@NotNull String templateIdentifier, @NotNull TextChannel channel) {
        LOG.info("Creating form access for template \"{}\".", templateIdentifier);
        FormAccess formAccess = new FormAccess(this, channel, templateIdentifier);

        this.entries.add(formAccess);

        LOG.debug("Registering form access \"{}\" as JDA listener.", templateIdentifier);
        channel.getJDA().addEventListener(formAccess);
    }

    public void submit(@NotNull String template, @NotNull IReplyCallback event) {
        // TODO
    }
}
