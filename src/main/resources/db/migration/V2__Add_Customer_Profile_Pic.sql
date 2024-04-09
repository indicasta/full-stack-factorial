ALTER TABLE customer
    ADD COLUMN profile_pic_id VARCHAR(36);

ALTER TABLE customer
    ADD CONSTRAINT  profile_pic_id_unique
        UNIQUE (profile_pic_id);