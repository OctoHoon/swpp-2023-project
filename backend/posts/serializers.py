from rest_framework import serializers
from django.contrib.auth import get_user_model
from .models import Post, PostPhoto, Restaurant


User= get_user_model()

class RestaurantSerializer(serializers.ModelSerializer):
    class Meta:
        model = Restaurant
        fields = '__all__'

class PostPhotoSerializer(serializers.ModelSerializer):
    class Meta:
        model = PostPhoto
        fields = '__all__'
        read_only_fields = ('post',)

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('username', 'id', 'avatar_url', 'description')
        read_only_fields = ('username', 'id', 'avatar_url','description')

class PostSerializer(serializers.ModelSerializer):
    restaurant = RestaurantSerializer()
    photos = PostPhotoSerializer(many=True, required=False)
    user = UserSerializer(read_only=True)

    class Meta:
        model = Post
        fields = '__all__'
        read_only_fields = ('user',)

    def create(self, validated_data):
        restaurant_data = validated_data.pop('restaurant')
        photos_data = validated_data.pop('photos', [])
        # Create or get a restaurant based on the name
        restaurant, created = Restaurant.objects.get_or_create(**restaurant_data)
        
        post = Post.objects.create(restaurant=restaurant, **validated_data)

        for photo_data in photos_data:
            PostPhoto.objects.create(post=post, **photo_data)

        return post

def data_list(serializer):
    class DataListSerializer(serializers.Serializer):
        data = serializer(many=True)
    
    return DataListSerializer
