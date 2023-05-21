# ChatEmojis
聊天表情包是一个轻量级插件，它用“表情包”代替符号。
ChatEmojis允许您在配置文件中添加自己的表情，并支持多个表情、随机表情符号选择和正则表达式捕获。聊天表情符号附带36个默认表情符号，包括Hypixel上的所有表情符号。

**需要 Java Version:** Java 8+

# 命令
| 命令 | 描述 | 权限 |
| ------  | ------ | ------ |
| `/emoji [list]` | 显示表情符号列表 | `chatemojis.list` |
| `/emoji help` | 显示命令列表 | none |
| `/emoji reload` | 重新加载所有表情符号 | `chatemojis.reload` |
| `/emoji version` | 显示插件的版本 | none |
| `/emoji settings` | 显示表情符号设置 | `chatemojis.admin` |

# 权限
| 权限节点 | 默认 | 描述
| ------ | ------ | ------ |
| chatemojis.list | 所有人 | Allowes access to use /emoji [list] |
| chatemojis.use.* | OP | Permission to use all emojis |
| chatemojis.reload | OP | Allowes access to reload emojis |
| chatemojis.admin | OP | Allowes access to change plugin settings |
| chatemojis.* | OP | Permission to use all emojis and /emoji command |

**Emoji-特定权限**:
未分组的表情符号权限就像“chatemojis.use”一样简单。<name>'
分组表情符号权限需要包含群组的路径（例如，'chatemojis.use.<group-path>.<name>'）
如果您仍然不了解如何获得特定于表情符号的权限 - 作为服务器操作员，您可以将鼠标悬停在表情符号上（在“/emoji”列表中）以查看该特定表情符号的权限节点。

# 依赖性
聊天表情符号不硬依赖任何其他插件;这意味着这些依赖项中的**没有**是ChatEmojis工作所必需的。
- **[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)** 用于占位符分析。
- **[EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/)** 用于解析直接消息中的表情符号。

请注意，如果您使用的是PlaceholderAPI，则还必须安装与您尝试访问的占位符相对应的PlaceholderAPI扩展，有关此内容的更多信息可以[此处]（https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders）。

# 如何创建自己的表情符号
有关如何创建自己的表情符号的完整教程和详细说明可以[这里]（https://github.com/Mxlvin/ChatEmojis/wiki/How-to-create-your-own-emoji）。

# 表情符号列表（截图）
![表情符号列表](https://i.imgur.com/B0s6wga.png)

# 许可证
本项目受 [GNU 通用公共许可证 v3.0]（https://github.com/Mxlvin/ChatEmojis/blob/main/LICENSE） 的约束。这仅适用于直接位于此干净存储库中的源代码。
对于那些不熟悉许可证的人，以下是其要点的摘要。这绝不是法律建议，也不具有法律约束力。
您可以
 -用
 -共享
 -修改

该项目全部或部分免费，甚至商业。但是，请考虑以下事项：

- **您必须披露您修改作品的源代码以及您从该项目中获取的源代码。这意味着不允许在闭源（甚至混淆）应用程序中（甚至部分）使用此项目中的代码。
 - **您修改后的应用程序也必须根据 GPL 获得许可**

执行上述操作并与所有人共享您的源代码;就像我们一样。
