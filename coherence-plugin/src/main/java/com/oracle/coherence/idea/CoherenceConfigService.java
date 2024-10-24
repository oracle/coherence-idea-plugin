package com.oracle.coherence.idea;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.oracle.coherence.idea.jps.CoherenceJpsProjectSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.jdom.Element;

@State(
        name = CoherenceJpsProjectSerializer.NAME,
        storages = {
                @Storage(CoherenceJpsProjectSerializer.CONFIG_FILE_NAME)
        }
)
@Service(Service.Level.PROJECT)
public final class CoherenceConfigService
    implements PersistentStateComponent<Element> {

    public CoherenceConfig getConfig()
        {
        return config;
        }

    @Override
    public @Nullable Element getState()
        {
        Element root = new Element("oracle-coherence");
        if (config != null)
            {
            config.saveTo(root);
            }
        return root;
        }

    @Override
    public void loadState(@NotNull Element root)
        {
        CoherenceConfig newConfig = new CoherenceConfig();
        newConfig.loadFrom(root);
        config = newConfig;
        }

    // ----- data members ---------------------------------------------------

    private CoherenceConfig config = new CoherenceConfig();
}
