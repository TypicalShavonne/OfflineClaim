package me.typical.offlineclaim.config.types;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

@Configuration
public class MessageConfig {

    @Comment("Message when a non-player tries to use the command")
    public String onlyPlayersCanUse = "§cLệnh dành cho người chơi";

    @Comment("Not admin notify")
    public String notAdmin = "§cKhông đủ quyền hạn";

    @Comment("Message when the player does not have permission to use the command")
    public String cannotSendToSelf = "§cBạn không thể gửi vật phẩm cho chính mình";

    @Comment("Message when the player name is invalid")
    public String invalidName = "§cTên người chơi không hợp lệ";

    @Comment("Message when the player is not holding any item")
    public String notHoldingItem = "§cBạn không cầm bất kỳ vật phẩm nào";

    @Comment("Message when the item is successfully sent to another player")
    public String itemSentToPlayer = "§aĐã gửi vật phẩm cho %s.";

    @Comment("Message when the player claims an item")
    public String itemClaimed = "§aĐã nhận vật phẩm thành công";

    @Comment("Message when the player have any claimable items")
    public String claimableItems = "§aBạn có vật phẩm có thể nhận. Sử dụng /claim để nhận.";
}
