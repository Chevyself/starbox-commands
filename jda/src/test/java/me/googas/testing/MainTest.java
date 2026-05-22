package me.googas.testing;

import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.jda.JdaAdapter;
import java.util.Arrays;
import javax.security.auth.login.LoginException;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class MainTest {

  public static void main(String[] args) throws LoginException, InterruptedException {
    JDA jda =
        JDABuilder.create(args[0], Arrays.asList(GatewayIntent.values())).build().awaitReady();
    jda.awaitReady();
    new CommandManagerBuilder<>(new JdaAdapter(jda, guild -> "-"))
        .build()
        .parseAndRegister(new TestCommands());
  }

  public static void deleteCommands(@NonNull JDA jda) {
    jda.retrieveCommands()
        .queue(commands -> commands.forEach(command -> jda.deleteCommandById(command.getIdLong())));
  }
}
