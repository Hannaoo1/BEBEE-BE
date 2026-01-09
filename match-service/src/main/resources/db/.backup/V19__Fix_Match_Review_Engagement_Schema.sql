SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE `match` ADD COLUMN `image_url` VARCHAR(255) NOT NULL DEFAULT '';
ALTER TABLE `match` ADD COLUMN `helper_review_id` BIGINT NULL;
ALTER TABLE `match` ADD COLUMN `disabled_review_id` BIGINT NULL;

ALTER TABLE `review` ADD COLUMN `match_id` BIGINT NOT NULL;

ALTER TABLE `review`
    ADD CONSTRAINT `FK_review_TO_match`
    FOREIGN KEY (`match_id`)
    REFERENCES `match` (`match_id`);

ALTER TABLE `match`
    ADD CONSTRAINT `FK_match_TO_helper_review`
    FOREIGN KEY (`helper_review_id`)
    REFERENCES `review` (`review_id`);

ALTER TABLE `match`
    ADD CONSTRAINT `FK_match_TO_disabled_review`
    FOREIGN KEY (`disabled_review_id`)
    REFERENCES `review` (`review_id`);

ALTER TABLE `engagement` CHANGE COLUMN `activity_date` `date` DATE NOT NULL;
ALTER TABLE `engagement` DROP COLUMN `completed_count`;
ALTER TABLE `engagement` ADD COLUMN `match_id` BIGINT NOT NULL;
ALTER TABLE `engagement` DROP FOREIGN KEY `FK_engagement_TO_agreement`;
ALTER TABLE `engagement` DROP COLUMN `agreement_id`;

-- Add FK from Engagement to Match
ALTER TABLE `engagement`
    ADD CONSTRAINT `FK_engagement_TO_match`
    FOREIGN KEY (`match_id`)
    REFERENCES `match` (`match_id`);

SET FOREIGN_KEY_CHECKS = 1;
