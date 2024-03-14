package ir.syphix.palladiumshop.utils;

import ir.syphix.palladiumshop.message.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {

    static MiniMessage miniMessage = MiniMessage.builder()
            .tags(
                    TagResolver.resolver(
                            TagResolver.standard()
                    )
            )
            .build();


    public static String toFormattedName(String name) {
        return Arrays.stream(name.split("_"))
                .map(t -> t.charAt(0) + t.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));

    }

    public static Component toFormattedComponent(String message, TagResolver... placeholders) {
        return Component.empty().decoration(TextDecoration.ITALIC, false).append(toComponent(String.format("%s%s", Messages.PREFIX, message), placeholders));
    }

    public static Component toComponent(String content, TagResolver... placeholders) {
        return miniMessage.deserialize(content, placeholders);
    }

}
