-- CREATING TABLES
CREATE TABLE shipments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tracking_number VARCHAR(64) NOT NULL UNIQUE,
    status VARCHAR(32) NOT NULL,
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    expected_delivery_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAUlt NOW()
);

CREATE TABLE shipment_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL UNIQUE,
    shipment_id UUID NOT NULL REFERENCES shipments(id) ON DELETE CASCADE,
    event_type VARCHAR(64) NOT NULL,
    carrier VARCHAR(64) NOT NULL,
    location VARCHAR(255),
    event_timestamp TIMESTAMPTZ NOT NULL,
    payload JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE delivery_exceptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shipment_id UUID NOT NULL REFERENCES shipments(id) ON DELETE CASCADE,
    exception_type VARCHAR(64) NOT NULL,
    reason TEXT,
    detected_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    resolved BOOLEAN NOT NULL DEFAULT FALSE,
    notified BOOLEAN NOT NULL DEFAULT FALSE
);

-- Adding Indexes
-- fast history lookups by parcel
CREATE INDEX idx_shipment_events_shipment_id ON shipment_events (shipment_id);

-- fast background polling for delayed deliveries
CREATE INDEX idx_shipment_delay_check on shipments (status, expected_delivery_at);

-- fast exception lookups by parcel
CREATE INDEX idx_delivery_exceptions_shipment_id on delivery_exceptions (shipment_id);
