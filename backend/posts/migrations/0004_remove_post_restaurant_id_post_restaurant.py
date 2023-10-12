# Generated by Django 4.2.6 on 2023-10-10 10:41

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('posts', '0003_restaurant_alter_post_restaurant_id'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='post',
            name='restaurant_id',
        ),
        migrations.AddField(
            model_name='post',
            name='restaurant',
            field=models.ForeignKey(db_column='restaurant_id', default=None, on_delete=django.db.models.deletion.CASCADE, related_name='posts', to='posts.restaurant'),
            preserve_default=False,
        ),
    ]