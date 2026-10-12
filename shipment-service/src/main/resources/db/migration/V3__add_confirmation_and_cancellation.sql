ALTER TABLE shipments
    ADD COLUMN booking_reference VARCHAR(255),
    ADD COLUMN capture_reference VARCHAR(255),
    ADD COLUMN confirmed_at TIMESTAMP(6) WITH TIME ZONE,
    ADD COLUMN cancellation_reason TEXT,
    ADD COLUMN cancelled_at TIMESTAMP(6) WITH TIME ZONE;