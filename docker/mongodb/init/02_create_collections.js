db = db.getSiblingDB('bebee');

db.createCollection("chat", {
    validator:{
        $jsonSchema: {
            bsonType: "object",
            required: ["_id", "chatroom_id", "type", "created_at"],
            properties: {
                "_id": {
                    bsonType: "long"
                },
                "chatroom_id": {
                    bsonType: "long"
                },
                "sender_id": {
                    bsonType: "long"
                },
                "type": {
                    bsonType: "string",
                    enum: ["TEXT", "IMAGE", "MATCH_SUCCESS", "MATCH_FAILURE", "MATCH_CONFIRMATION"]
                },
                "text_content": {
                    bsonType: "string"
                },
                "attachments": {
                    bsonType: "array",
                    items: {
                        bsonType: "string"
                    }
                },
                "match_confirmation_content": {
                    bsonType: "object",
                    required: ["type", "start_date", "schedules", "region", "help_category_ids", "status"],
                    properties: {
                        "agreement_id": {
                            bsonType: "long"
                        },
                        "disabled_id": {
                            bsonType: "long",
                            description: "도움 요청자 ID"
                        },
                        "helper_id": {
                            bsonType: "long",
                            description: "도우미 ID"
                        },
                        "is_volunteer": {
                            bsonType: "bool",
                            description: "나눔 여부 (true: 봉사, false: 유료)"
                        },
                        "type": {
                            bsonType: "string",
                            enum: ["DAY", "TERM"]
                        },
                        "start_date": {
                            bsonType: "string",
                            description: "YYYY-MM-DD"
                        },
                        "end_date": {
                            bsonType: "string",
                            description: "YYYY-MM-DD"
                        },
                        "schedules": {
                            bsonType: "array",
                            items: {
                                bsonType: "object",
                                required: ["day", "start_time", "end_time"],
                                properties: {
                                    "day": {
                                        bsonType: "string",
                                        enum: ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"]
                                    },
                                    "start_time": {
                                        bsonType: "string",
                                        description: "HH:mm"
                                    },
                                    "end_time": {
                                        bsonType: "string",
                                        description: "HH:mm"
                                    }
                                }
                            }
                        },
                        "region": {
                            bsonType: "string",
                            description: "만남 장소"
                        },
                        "points": {
                            bsonType: "object",
                            properties: {
                                "unit_honey": {
                                    bsonType: "int",
                                    description: "단위 포인트 (시간당 허니)"
                                },
                                "total_honey": {
                                    bsonType: "int",
                                    description: "총 포인트 (총 허니)"
                                }
                            }
                        },
                        "help_category_ids": {
                            bsonType: "array",
                            items: {
                                bsonType: "long"
                            },
                            description: "도움 카테고리 ID 목록"
                        },
                        "status": {
                            bsonType: "string",
                            enum: ["NON_MATCHED", "PROCEEDING", "MATCHED"]
                        }
                    }
                },
                "created_at": {
                    bsonType: "date"
                }
            }
        }
    }
})

db.createCollection("last_read_chat", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["_id", "chat_room_id", "reader_id", "last_read_chat_id", "created_at", "updated_at"],
            properties: {
                "_id": {
                    bsonType: "long"
                },
                "chat_room_id": {
                    bsonType: "long"
                },
                "reader_id": {
                    bsonType: "long"
                },
                "last_read_chat_id": {
                    bsonType: "long"
                },
                "created_at": {
                    bsonType: "date"
                },
                "updated_at": {
                    bsonType: "date"
                }
            }
        }
    }
});