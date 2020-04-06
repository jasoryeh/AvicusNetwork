-- Generated from migrated Avicus Website Database
-- 
-- Generation Time: Apr 06, 2020 at 03:59 PM
-- Generated from server type: MariaDB

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `atlas`
--

-- --------------------------------------------------------

--
-- Table structure for table `achievements`
--

CREATE TABLE `achievements` (
  `id` int(11) NOT NULL,
  `slug` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `achievement_pursuits`
--

CREATE TABLE `achievement_pursuits` (
  `id` int(11) NOT NULL,
  `slug` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `progress` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `achievement_receivers`
--

CREATE TABLE `achievement_receivers` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `achievement_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `actions`
--

CREATE TABLE `actions` (
  `id` int(11) NOT NULL,
  `action` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `text` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `appeal_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `alerts`
--

CREATE TABLE `alerts` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `seen` int(11) DEFAULT 0,
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `announcements`
--

CREATE TABLE `announcements` (
  `id` int(11) NOT NULL,
  `body` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `motd` tinyint(1) DEFAULT NULL,
  `lobby` tinyint(1) DEFAULT NULL,
  `tips` tinyint(1) DEFAULT NULL,
  `web` tinyint(1) DEFAULT NULL,
  `popup` tinyint(1) DEFAULT NULL,
  `permission` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `motd_format` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `appeals`
--

CREATE TABLE `appeals` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `punishment_id` int(11) DEFAULT NULL,
  `open` tinyint(1) DEFAULT NULL,
  `locked` tinyint(1) DEFAULT NULL,
  `appealed` tinyint(1) DEFAULT NULL,
  `escalated` tinyint(1) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ar_internal_metadata`
--

CREATE TABLE `ar_internal_metadata` (
  `key` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `backpack_gadgets`
--

CREATE TABLE `backpack_gadgets` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `gadget_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gadget` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `context` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `old_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `blazer_audits`
--

CREATE TABLE `blazer_audits` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `query_id` int(11) DEFAULT NULL,
  `statement` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `data_source` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `blazer_checks`
--

CREATE TABLE `blazer_checks` (
  `id` int(11) NOT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `query_id` int(11) DEFAULT NULL,
  `state` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `schedule` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `emails` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `check_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_run_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `blazer_dashboards`
--

CREATE TABLE `blazer_dashboards` (
  `id` int(11) NOT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `name` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `blazer_dashboard_queries`
--

CREATE TABLE `blazer_dashboard_queries` (
  `id` int(11) NOT NULL,
  `dashboard_id` int(11) DEFAULT NULL,
  `query_id` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `blazer_queries`
--

CREATE TABLE `blazer_queries` (
  `id` int(11) NOT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `statement` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `data_source` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `forum_id` int(11) DEFAULT NULL,
  `desc` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tags` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `exclude_from_recent` tinyint(1) DEFAULT NULL,
  `uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `credits`
--

CREATE TABLE `credits` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `weight` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `deaths`
--

CREATE TABLE `deaths` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `cause` int(11) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `user_hidden` tinyint(1) DEFAULT 0,
  `cause_hidden` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `discussions`
--

CREATE TABLE `discussions` (
  `id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `archived` int(11) DEFAULT 0,
  `stickied` int(11) DEFAULT 0,
  `category_id` int(11) DEFAULT NULL,
  `uuid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `views` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `experience_leaderboard_entries`
--

CREATE TABLE `experience_leaderboard_entries` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `period` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `prestige_level` int(11) DEFAULT NULL,
  `xp_total` int(11) DEFAULT NULL,
  `xp_nebula` int(11) DEFAULT NULL,
  `xp_koth` int(11) DEFAULT NULL,
  `xp_ctf` int(11) DEFAULT NULL,
  `xp_tdm` int(11) DEFAULT NULL,
  `xp_elimination` int(11) DEFAULT NULL,
  `xp_sw` int(11) DEFAULT NULL,
  `xp_walls` int(11) DEFAULT NULL,
  `xp_arcade` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `experience_transactions`
--

CREATE TABLE `experience_transactions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `season_id` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `weight` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `genre` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `forums`
--

CREATE TABLE `forums` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `friend_id` int(11) DEFAULT NULL,
  `accepted` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `impressions`
--

CREATE TABLE `impressions` (
  `id` int(11) NOT NULL,
  `impressionable_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `impressionable_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `controller_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `action_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `view_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `request_hash` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ip_address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `session_hash` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `message` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `referrer` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `params` text COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ip_bans`
--

CREATE TABLE `ip_bans` (
  `id` int(11) NOT NULL,
  `staff_id` int(11) DEFAULT NULL,
  `reason` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ip` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `excluded_users` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `leaderboard_entries`
--

CREATE TABLE `leaderboard_entries` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `period` int(11) DEFAULT NULL,
  `kills` int(11) DEFAULT NULL,
  `deaths` int(11) DEFAULT NULL,
  `kd_ratio` float DEFAULT NULL,
  `monuments` int(11) DEFAULT NULL,
  `wools` int(11) DEFAULT NULL,
  `flags` int(11) DEFAULT NULL,
  `hills` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `time_online` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `livestreams`
--

CREATE TABLE `livestreams` (
  `id` int(11) NOT NULL,
  `channel` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `map_ratings`
--

CREATE TABLE `map_ratings` (
  `id` int(11) NOT NULL,
  `map_slug` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `map_version` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `player` int(11) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `feedback` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `memberships`
--

CREATE TABLE `memberships` (
  `id` int(11) NOT NULL,
  `rank_id` int(11) DEFAULT NULL,
  `member_id` int(11) DEFAULT NULL,
  `expires_at` datetime DEFAULT NULL,
  `is_purchased` tinyint(1) DEFAULT 0,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `role` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `receiver_id` int(11) DEFAULT NULL,
  `content` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `objectives`
--

CREATE TABLE `objectives` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `objective_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `hidden` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `objective_types`
--

CREATE TABLE `objective_types` (
  `id` int(11) NOT NULL,
  `game_id` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `presents`
--

CREATE TABLE `presents` (
  `id` bigint(20) NOT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `family` varchar(255) DEFAULT NULL,
  `human_name` varchar(255) DEFAULT NULL,
  `human_location` varchar(255) DEFAULT NULL,
  `found_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `present_finders`
--

CREATE TABLE `present_finders` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `present_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `prestige_levels`
--

CREATE TABLE `prestige_levels` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `season_id` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `prestige_seasons`
--

CREATE TABLE `prestige_seasons` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `multiplier` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `start_at` datetime DEFAULT NULL,
  `end_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `punishments`
--

CREATE TABLE `punishments` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `staff_id` int(11) DEFAULT NULL,
  `type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reason` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `expires` datetime DEFAULT NULL,
  `appealed` int(11) DEFAULT 0,
  `server_id` int(11) DEFAULT NULL,
  `silent` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `purchases`
--

CREATE TABLE `purchases` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `p_id` int(11) DEFAULT 0,
  `duration` int(11) DEFAULT NULL,
  `expired` int(11) DEFAULT 0,
  `spent` int(11) DEFAULT 0,
  `created_at` datetime DEFAULT NULL,
  `product_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `data` text COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ranks`
--

CREATE TABLE `ranks` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mc_perms` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `web_perms` longtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_staff` tinyint(1) DEFAULT 0,
  `html_color` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'none',
  `badge_color` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'none',
  `badge_text_color` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'white',
  `mc_prefix` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `mc_suffix` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `priority` int(11) DEFAULT 0,
  `special_perms` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `inheritance_id` int(11) DEFAULT NULL,
  `ts_perms` text COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `read_marks`
--

CREATE TABLE `read_marks` (
  `id` int(11) NOT NULL,
  `readable_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `readable_type` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `timestamp` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `registrations`
--

CREATE TABLE `registrations` (
  `id` int(11) NOT NULL,
  `tournament_id` int(11) DEFAULT NULL,
  `team_id` int(11) DEFAULT NULL,
  `user_data` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `replies`
--

CREATE TABLE `replies` (
  `id` int(11) NOT NULL,
  `discussion_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `reply_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reports`
--

CREATE TABLE `reports` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `reason` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `server` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reserved_slots`
--

CREATE TABLE `reserved_slots` (
  `id` int(11) NOT NULL,
  `team_id` int(11) DEFAULT NULL,
  `server` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `start_at` datetime DEFAULT NULL,
  `end_at` datetime DEFAULT NULL,
  `reservee` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `revisions`
--

CREATE TABLE `revisions` (
  `id` int(11) NOT NULL,
  `reply_id` int(11) DEFAULT NULL,
  `discussion_id` int(11) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `body` mediumtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `archived` int(11) DEFAULT 0,
  `deleted` int(11) DEFAULT 0,
  `stickied` int(11) DEFAULT 0,
  `locked` int(11) DEFAULT 0,
  `active` int(11) DEFAULT 0,
  `original` int(11) DEFAULT 0,
  `tag` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sanctioned` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `schema_migrations`
--

CREATE TABLE `schema_migrations` (
  `version` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `servers`
--

CREATE TABLE `servers` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `host` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `permissible` tinyint(1) DEFAULT NULL,
  `auto_deploy` tinyint(1) DEFAULT NULL,
  `path` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `screen_session` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `server_group_id` int(11) DEFAULT NULL,
  `server_category_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `server_boosters`
--

CREATE TABLE `server_boosters` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `multiplier` decimal(10,0) DEFAULT NULL,
  `starts_at` datetime DEFAULT NULL,
  `expires_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `server_categories`
--

CREATE TABLE `server_categories` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `communication_options` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `tracking_options` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `infraction_options` text COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `server_groups`
--

CREATE TABLE `server_groups` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `slug` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `data` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `icon` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

CREATE TABLE `sessions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `duration` int(11) DEFAULT 0,
  `ip` varchar(36) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `graceful` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `key` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `subscriptions`
--

CREATE TABLE `subscriptions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `discussion_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `teams`
--

CREATE TABLE `teams` (
  `id` int(11) NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tag` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tagline` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `about` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 COLLATE=utf8_unicode_ci DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- Table structure for table `teamspeak_users`
--

CREATE TABLE `teamspeak_users` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `team_members`
--

CREATE TABLE `team_members` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `role` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `team_id` int(11) DEFAULT NULL,
  `accepted` int(11) DEFAULT 0,
  `accepted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tournaments`
--

CREATE TABLE `tournaments` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `slug` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `about` text COLLATE utf8_unicode_ci DEFAULT NULL,
  `open_at` datetime DEFAULT NULL,
  `close_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `header` int(11) DEFAULT NULL,
  `min` int(11) DEFAULT NULL,
  `max` int(11) DEFAULT NULL,
  `allow_loners` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `usernames`
--

CREATE TABLE `usernames` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `username` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `uuid` varchar(36) COLLATE utf8_unicode_ci DEFAULT NULL,
  `locale` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tracker` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mc_version` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `password_secure` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `verify_key` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `verify_key_success` tinyint(1) DEFAULT NULL,
  `discord_id` bigint(20) DEFAULT NULL,
  `api_key` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_details`
--

CREATE TABLE `user_details` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email_status` int(11) DEFAULT 0,
  `avatar` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `about` mediumtext COLLATE utf8_unicode_ci DEFAULT NULL,
  `cover_art` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `interests` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gender` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `skype` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `twitter` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `facebook` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `twitch` varchar(26) COLLATE utf8_unicode_ci DEFAULT NULL,
  `steam` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `github` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `discord` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `custom_badge_icon` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `custom_badge_color` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `instagram` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `votes`
--

CREATE TABLE `votes` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `service` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cast_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `achievements`
--
ALTER TABLE `achievements`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `achievement_pursuits`
--
ALTER TABLE `achievement_pursuits`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_achievement_pursuits_on_user_id` (`user_id`);

--
-- Indexes for table `achievement_receivers`
--
ALTER TABLE `achievement_receivers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_achievement_receivers_on_achievement_id` (`achievement_id`),
  ADD KEY `index_achievement_receivers_on_user_id` (`user_id`);

--
-- Indexes for table `actions`
--
ALTER TABLE `actions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_actions_on_appeal_id` (`appeal_id`);

--
-- Indexes for table `alerts`
--
ALTER TABLE `alerts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `announcements`
--
ALTER TABLE `announcements`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `appeals`
--
ALTER TABLE `appeals`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ar_internal_metadata`
--
ALTER TABLE `ar_internal_metadata`
  ADD PRIMARY KEY (`key`);

--
-- Indexes for table `backpack_gadgets`
--
ALTER TABLE `backpack_gadgets`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_backpack_gadgets_on_user_id` (`user_id`);

--
-- Indexes for table `blazer_audits`
--
ALTER TABLE `blazer_audits`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blazer_checks`
--
ALTER TABLE `blazer_checks`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blazer_dashboards`
--
ALTER TABLE `blazer_dashboards`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blazer_dashboard_queries`
--
ALTER TABLE `blazer_dashboard_queries`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blazer_queries`
--
ALTER TABLE `blazer_queries`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_categories_on_forum_id` (`forum_id`);

--
-- Indexes for table `credits`
--
ALTER TABLE `credits`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `deaths`
--
ALTER TABLE `deaths`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cause` (`cause`),
  ADD KEY `index_deaths_on_cause_hidden` (`cause_hidden`),
  ADD KEY `game_id` (`game_id`),
  ADD KEY `index_deaths_on_user_hidden` (`user_hidden`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `discussions`
--
ALTER TABLE `discussions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `category_id` (`category_id`),
  ADD KEY `created_at` (`created_at`);

--
-- Indexes for table `experience_leaderboard_entries`
--
ALTER TABLE `experience_leaderboard_entries`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_experience_leaderboard_entries_on_user_id` (`user_id`);

--
-- Indexes for table `experience_transactions`
--
ALTER TABLE `experience_transactions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `forums`
--
ALTER TABLE `forums`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `friends`
--
ALTER TABLE `friends`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `impressions`
--
ALTER TABLE `impressions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `controlleraction_ip_index` (`controller_name`,`action_name`,`ip_address`),
  ADD KEY `controlleraction_request_index` (`controller_name`,`action_name`,`request_hash`),
  ADD KEY `controlleraction_session_index` (`controller_name`,`action_name`,`session_hash`),
  ADD KEY `poly_ip_index` (`impressionable_type`,`impressionable_id`,`ip_address`),
  ADD KEY `poly_params_request_index` (`impressionable_type`,`impressionable_id`,`params`(255)),
  ADD KEY `poly_request_index` (`impressionable_type`,`impressionable_id`,`request_hash`),
  ADD KEY `poly_session_index` (`impressionable_type`,`impressionable_id`,`session_hash`),
  ADD KEY `impressionable_type_message_index` (`impressionable_type`,`message`(255),`impressionable_id`),
  ADD KEY `index_impressions_on_user_id` (`user_id`);

--
-- Indexes for table `ip_bans`
--
ALTER TABLE `ip_bans`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `leaderboard_entries`
--
ALTER TABLE `leaderboard_entries`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_leaderboard_entries_on_user_id` (`user_id`);

--
-- Indexes for table `livestreams`
--
ALTER TABLE `livestreams`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `map_ratings`
--
ALTER TABLE `map_ratings`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `memberships`
--
ALTER TABLE `memberships`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_memberships_on_rank_id` (`rank_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_messages_on_receiver_id` (`receiver_id`),
  ADD KEY `index_messages_on_sender_id` (`sender_id`);

--
-- Indexes for table `objectives`
--
ALTER TABLE `objectives`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_objectives_on_hidden` (`hidden`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `objective_types`
--
ALTER TABLE `objective_types`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `presents`
--
ALTER TABLE `presents`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `present_finders`
--
ALTER TABLE `present_finders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_present_finders_on_present_id` (`present_id`),
  ADD KEY `index_present_finders_on_user_id` (`user_id`);

--
-- Indexes for table `prestige_levels`
--
ALTER TABLE `prestige_levels`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `prestige_seasons`
--
ALTER TABLE `prestige_seasons`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `punishments`
--
ALTER TABLE `punishments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_punishments_on_server_id` (`server_id`);

--
-- Indexes for table `purchases`
--
ALTER TABLE `purchases`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ranks`
--
ALTER TABLE `ranks`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `read_marks`
--
ALTER TABLE `read_marks`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_read_marks_on_user_id_and_readable_type_and_readable_id` (`user_id`,`readable_type`,`readable_id`);

--
-- Indexes for table `registrations`
--
ALTER TABLE `registrations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_registrations_on_team_id` (`team_id`),
  ADD KEY `index_registrations_on_tournament_id` (`tournament_id`);

--
-- Indexes for table `replies`
--
ALTER TABLE `replies`
  ADD PRIMARY KEY (`id`),
  ADD KEY `created_at` (`created_at`);

--
-- Indexes for table `reports`
--
ALTER TABLE `reports`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reserved_slots`
--
ALTER TABLE `reserved_slots`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `revisions`
--
ALTER TABLE `revisions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `active` (`active`),
  ADD KEY `discussion_id` (`discussion_id`),
  ADD KEY `original` (`original`),
  ADD KEY `reply_id` (`reply_id`);

--
-- Indexes for table `schema_migrations`
--
ALTER TABLE `schema_migrations`
  ADD PRIMARY KEY (`version`);

--
-- Indexes for table `servers`
--
ALTER TABLE `servers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `server_boosters`
--
ALTER TABLE `server_boosters`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_server_boosters_on_server_id` (`server_id`),
  ADD KEY `index_server_boosters_on_user_id` (`user_id`);

--
-- Indexes for table `server_categories`
--
ALTER TABLE `server_categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `server_groups`
--
ALTER TABLE `server_groups`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ip` (`ip`),
  ADD KEY `index_sessions_on_server_id` (`server_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `subscriptions`
--
ALTER TABLE `subscriptions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `teamspeak_users`
--
ALTER TABLE `teamspeak_users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `team_members`
--
ALTER TABLE `team_members`
  ADD PRIMARY KEY (`id`),
  ADD KEY `team_id` (`team_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `tournaments`
--
ALTER TABLE `tournaments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `usernames`
--
ALTER TABLE `usernames`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `username` (`username`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_users_on_api_key` (`api_key`),
  ADD KEY `username` (`username`),
  ADD KEY `uuid` (`uuid`);

--
-- Indexes for table `user_details`
--
ALTER TABLE `user_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `votes`
--
ALTER TABLE `votes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_votes_on_user_id` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `achievements`
--
ALTER TABLE `achievements`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `achievement_pursuits`
--
ALTER TABLE `achievement_pursuits`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `achievement_receivers`
--
ALTER TABLE `achievement_receivers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `actions`
--
ALTER TABLE `actions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `alerts`
--
ALTER TABLE `alerts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `announcements`
--
ALTER TABLE `announcements`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `appeals`
--
ALTER TABLE `appeals`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `backpack_gadgets`
--
ALTER TABLE `backpack_gadgets`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `blazer_audits`
--
ALTER TABLE `blazer_audits`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `blazer_checks`
--
ALTER TABLE `blazer_checks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `blazer_dashboards`
--
ALTER TABLE `blazer_dashboards`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `blazer_dashboard_queries`
--
ALTER TABLE `blazer_dashboard_queries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `blazer_queries`
--
ALTER TABLE `blazer_queries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `credits`
--
ALTER TABLE `credits`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `deaths`
--
ALTER TABLE `deaths`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `discussions`
--
ALTER TABLE `discussions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `experience_leaderboard_entries`
--
ALTER TABLE `experience_leaderboard_entries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `experience_transactions`
--
ALTER TABLE `experience_transactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `forums`
--
ALTER TABLE `forums`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `friends`
--
ALTER TABLE `friends`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `impressions`
--
ALTER TABLE `impressions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ip_bans`
--
ALTER TABLE `ip_bans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `leaderboard_entries`
--
ALTER TABLE `leaderboard_entries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `livestreams`
--
ALTER TABLE `livestreams`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `map_ratings`
--
ALTER TABLE `map_ratings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `memberships`
--
ALTER TABLE `memberships`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `objectives`
--
ALTER TABLE `objectives`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `objective_types`
--
ALTER TABLE `objective_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `presents`
--
ALTER TABLE `presents`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `present_finders`
--
ALTER TABLE `present_finders`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `prestige_levels`
--
ALTER TABLE `prestige_levels`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `prestige_seasons`
--
ALTER TABLE `prestige_seasons`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `punishments`
--
ALTER TABLE `punishments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `purchases`
--
ALTER TABLE `purchases`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ranks`
--
ALTER TABLE `ranks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `read_marks`
--
ALTER TABLE `read_marks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `registrations`
--
ALTER TABLE `registrations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `replies`
--
ALTER TABLE `replies`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `reports`
--
ALTER TABLE `reports`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `reserved_slots`
--
ALTER TABLE `reserved_slots`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `revisions`
--
ALTER TABLE `revisions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `servers`
--
ALTER TABLE `servers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `server_boosters`
--
ALTER TABLE `server_boosters`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `server_categories`
--
ALTER TABLE `server_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `server_groups`
--
ALTER TABLE `server_groups`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sessions`
--
ALTER TABLE `sessions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `settings`
--
ALTER TABLE `settings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `subscriptions`
--
ALTER TABLE `subscriptions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `teams`
--
ALTER TABLE `teams`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `teamspeak_users`
--
ALTER TABLE `teamspeak_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `team_members`
--
ALTER TABLE `team_members`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tournaments`
--
ALTER TABLE `tournaments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `usernames`
--
ALTER TABLE `usernames`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user_details`
--
ALTER TABLE `user_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `votes`
--
ALTER TABLE `votes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
